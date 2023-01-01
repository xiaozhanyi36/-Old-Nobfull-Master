// Destiny made by ChengFeng
package net.ccbluex.liquidbounce


import net.ccbluex.liquidbounce.event.ClientShutdownEvent
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.macro.MacroManager
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.CombatManager
import net.ccbluex.liquidbounce.features.special.ServerSpoof
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.file.MetricsLite
import net.ccbluex.liquidbounce.file.config.ConfigManager
import net.ccbluex.liquidbounce.script.ScriptManager
import net.ccbluex.liquidbounce.script.remapper.Remapper
import net.ccbluex.liquidbounce.ui.cape.GuiCapeManager
import net.ccbluex.liquidbounce.ui.client.mainmenu.GuiLogin
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGUIModule
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGuiConfig
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.liquidbounce.ClickGui
import net.ccbluex.liquidbounce.ui.client.hud.HUD
import net.ccbluex.liquidbounce.ui.client.keybind.KeyBindManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.ui.i18n.LanguageManager
import net.ccbluex.liquidbounce.ui.sound.TipSoundManager
import net.ccbluex.liquidbounce.ui.client.mainmenu.GuiWelcomeBack
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.HWID.Checker
import net.ccbluex.liquidbounce.utils.HWID.WebUtils
import net.ccbluex.liquidbounce.utils.InventoryUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.FMLCommonHandler
import java.io.File


object LiquidBounce {


    // Client information
    const val CLIENT_NAME = "NobleFull"
    const val COLORED_NAME = "§c§lNobleFull"
    const val CLIENT_VERSION = "043022"
    const val CLIENT_CREATOR = "Silent & CCbluex & TIQS"
    const val CLIENT_WEBSITE = "NobleFull.today"
    const val QQ_GROUP = 3398229467
    const val MINECRAFT_VERSION = "1.8.9"
    lateinit var rankedName: String
    lateinit var cleanName: String

    var isStarting = true
    var isLoadingConfig = true

    // Managers
    lateinit var moduleManager: ModuleManager
    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var fileManager: FileManager
    lateinit var scriptManager: ScriptManager
    lateinit var tipSoundManager: TipSoundManager
    lateinit var combatManager: CombatManager
    lateinit var macroManager: MacroManager
    lateinit var configManager: ConfigManager

    // Some UI things
    lateinit var hud: HUD
    lateinit var mainMenu: GuiScreen
    lateinit var keyBindManager: KeyBindManager
    private lateinit var metricsLite: MetricsLite
    lateinit var clickGui: ClickGui
    lateinit var GuiWelcomeBack: GuiScreen

    lateinit var clickGuiConfig: ClickGuiConfig

    var theme = net.ccbluex.liquidbounce.features.module.modules.color.HUD.clientColorMode.get()

    // Menu Background
    var background: ResourceLocation? = null
    var usingBackground = false
    lateinit var userList: String

    // Hwid
    var verified = false

    //mixin packet fix
    var fixC08pack112=false;


    /**
     * Execute if client will be started
     */
    fun initClient() {
        if (!Checker.IllIlIlIllllIIlIl()) {
            usingBackground = false
            verified = false
            cleanName = "Why are you reading this?"
            FMLCommonHandler.instance().exitJava(0, false)
        } else {
            verified = true
            ClientUtils.logInfo("Loading $CLIENT_NAME #$CLIENT_VERSION, by $CLIENT_CREATOR")
            userList = WebUtils.get("https://raw.fastgit.org/xiaozhanyi36/hwid_noblefull/main/hwid.txt")

            val startTime = System.currentTimeMillis()

            // Create file manager
            fileManager = FileManager()
            configManager = ConfigManager()

            // Create event manager
            eventManager = EventManager()

            // Load language
            LanguageManager.loadLanguage()

            // Register listeners
            eventManager.registerListener(RotationUtils())
            eventManager.registerListener(AntiForge)
            eventManager.registerListener(InventoryUtils)
            eventManager.registerListener(ServerSpoof)

            // Create command manager
            commandManager = CommandManager()

            fileManager.loadConfigs(fileManager.accountsConfig, fileManager.friendsConfig, fileManager.specialConfig)

            // Load client fonts
            Fonts.loadFonts()

            macroManager = MacroManager()
            eventManager.registerListener(macroManager)

            // Setup module manager and register modules
            moduleManager = ModuleManager()
            moduleManager.registerModules()


            // Remapper
            try {
                Remapper.loadSrg()

                // ScriptManager
                scriptManager = ScriptManager()
                scriptManager.loadScripts()
                scriptManager.enableScripts()
            } catch (throwable: Throwable) {
                ClientUtils.logError("Failed to load scripts.", throwable)
            }

            // Register commands
            commandManager.registerCommands()

            tipSoundManager = TipSoundManager()

            // KeyBindManager
            keyBindManager = KeyBindManager()

            // bstats.org user count display
            metricsLite = MetricsLite(11076)

            combatManager = CombatManager()
            eventManager.registerListener(combatManager)

            GuiCapeManager.load()

            // Set HUD
            hud = HUD.createDefault()
            usingBackground = true

            fileManager.loadConfigs(fileManager.hudConfig, fileManager.xrayConfig)

            ClientUtils.logInfo("$CLIENT_NAME $CLIENT_VERSION loaded in ${(System.currentTimeMillis() - startTime)}ms!")
            ClientUtils.setTitle()
            startClient()
        }
    }

    /**
     * Execute if client ui type is selected
     */
    private fun startClient() {
        mainMenu = GuiLogin()
        moduleManager.registerModule(ClickGUIModule())

        clickGui = ClickGui()
        clickGuiConfig = ClickGuiConfig(
            File(
                fileManager.dir,
                "Clickgui.json"
            )
        )
        fileManager.loadConfig(clickGuiConfig)

        // Load configs
        configManager.loadLegacySupport()
        configManager.loadConfigSet()

        // Set is starting status
        isStarting = false
        isLoadingConfig = false
        Minecraft.getMinecraft().displayGuiScreen(mainMenu)
        ClientUtils.logInfo("$CLIENT_NAME $CLIENT_VERSION started!")
    }

    /**
     * Execute if client will be stopped
     */
    fun stopClient() {
        if (!isStarting && !isLoadingConfig) {
            ClientUtils.logInfo("Shutting down $CLIENT_NAME $CLIENT_VERSION!")

            // Call client shutdown
            eventManager.callEvent(ClientShutdownEvent())

            // Save all available configs
            GuiCapeManager.save()
            configManager.save(saveConfigSet = true, forceSave = true)
            fileManager.saveAllConfigs()

            fileManager.saveConfig(clickGuiConfig)
        }
    }
}
