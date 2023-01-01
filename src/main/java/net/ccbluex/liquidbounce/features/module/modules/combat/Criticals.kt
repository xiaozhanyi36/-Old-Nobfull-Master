// Destiny made by ChengFeng
package net.ccbluex.liquidbounce.features.module.modules.combat

import by.radioegor146.nativeobfuscator.Native
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.utils.MathUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils.sendPacketNoEvent
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import  net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.java.games.input.Keyboard
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.server.S0BPacketAnimation
import net.minecraft.stats.StatList
import org.omg.CORBA.portable.CustomValue
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random


@ModuleInfo(name = "Criticals", category = ModuleCategory.COMBAT, description = "Automatically deals critical hits")
@Native
class
Criticals : Module() {

    val modeValue = ListValue(
        "Mode",
        arrayOf(
            "NewPacket",
            "MiPacket",
            "MiniPhase",
            "NanoPacket",
            "NoGround",
            "Minemora",
            "Motion",
            "AACPacket",
            "NCPPacket",
            "Hytnew",
            "TpHop",
            "Randompacket",
            "NewVerus",
            "FakeCollide",
            "Watchdog",
            "MatrixH",
            "VulcanSemi",
            "HytPacket"
            ),
        "Hytnew"
    )
    private val motionValue =
        ListValue(
            "MotionMode",
            arrayOf("Hop", "Jump", "LowJump", "Custom","VulcanHop"), "Jump").displayable { modeValue.equals("Motion") }
    val delayValue = IntegerValue("Delay", 120, 0, 500)
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    private val packetValue = ListValue("Packet", arrayOf("C04", "C06"), "C04")
    private val postValue = BoolValue("HypixelPost", false).displayable { modeValue.equals("Hypixel") }
    private val onlyAuraValue = BoolValue("OnlyAura", true)
    private val debugValue = BoolValue("CriticalMessage", true)
    private val motionYValue = FloatValue("MotionY", 0.42f, 0.01f, 1.0f).displayable { motionValue.equals("Custom") }
    private val upTimerValue = FloatValue("CustomUpTimer",1.0f , 1.0f , 10.0f).displayable { motionValue.equals("Custom") }
    private val downTimerValue = FloatValue("CustomDownTimer",1.0f , 1.0f , 10.0f ).displayable { motionValue.equals("Custom") }
    private val minRandomPacketValue : FloatValue = object : FloatValue("MinRandomPacket",0.000000000000000122774287821489214F,0F,1.0F)  {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if(newValue > maxRandomPacketValue.get())
                set(maxRandomPacketValue.get())
            if(newValue < minimum)
                set(minimum)
            if(newValue > maximum)
                set(maximum)
        }
    }
    private val maxRandomPacketValue : FloatValue = object : FloatValue("MaxRandomPacket", 0.00000000000042638121217822151F, 0F, 1.0F) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if(newValue < minRandomPacketValue.get())
                set(minRandomPacketValue.get())
            if(newValue < minimum)
                set(minimum)
            if(newValue > maximum)
                set(maximum)
        }
    }
    val msTimer = MSTimer()
    var attacks = 0
    private var target = 0
    private var jState = 0
    private var counter = 0
    private var lastY = -1.0
    private val y1 = doubleArrayOf(0.104080378093037, 0.105454222033912, 0.102888018147468, 0.099634532004642)

    override fun onEnable() {
        if (modeValue.equals("NoGround")) {
            mc.thePlayer.jump()
        }
        jState = 0
        counter = 0
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        val x7 = mc.thePlayer.posX
        val y7 = mc.thePlayer.posY
        val z7 = mc.thePlayer.posZ

        if (onlyAuraValue.get() && !LiquidBounce.moduleManager[KillAura::class.java]!!.state) return

        if (event.targetEntity is EntityLivingBase) {
            val entity = event.targetEntity
            target = entity.entityId

            if (!mc.thePlayer.onGround || mc.thePlayer.isOnLadder || mc.thePlayer.isInWeb || mc.thePlayer.isInWater ||
                mc.thePlayer.isInLava || mc.thePlayer.ridingEntity != null || entity.hurtTime > hurtTimeValue.get() ||
                LiquidBounce.moduleManager[Fly::class.java]!!.state || !msTimer.hasTimePassed(delayValue.get().toLong())
            ) {
                return
            }


            fun sendCriticalPacket(
                xOffset: Double = 0.0,
                yOffset: Double = 0.0,
                zOffset: Double = 0.0,
                ground: Boolean
            ) {  val x = mc.thePlayer.posX + xOffset
                val y = mc.thePlayer.posY + yOffset
                val z = mc.thePlayer.posZ + zOffset

                if (packetValue.equals("C06")) {
                    mc.netHandler.addToSendQueue(
                        C06PacketPlayerPosLook(
                            x,
                            y,
                            z,
                            mc.thePlayer.rotationYaw,
                            mc.thePlayer.rotationPitch,
                            ground
                        )
                    )
                } else {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, ground))
                }
            }

            when (modeValue.get().lowercase()) {

                "hytpacket" -> {
                    val posy = doubleArrayOf(0.00150000001304,0.0140000001304,0.001500001304)
                    for (i in posy.indices) {
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x7, y7 +  0.0525000001304, z7, true))
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x7, y7 + posy[i], z7, false))
                    }
                    mc.thePlayer.onCriticalHit(entity)
                }
                "vulcansemi" -> {
                    attacks++
                    if(attacks > 6) {
                        sendCriticalPacket(yOffset = 0.2, ground = false)
                        sendCriticalPacket(yOffset = 0.1216, ground = false)
                        attacks = 0
                    }
                }
                "matrixH" -> {
                    if (mc.thePlayer.ticksExisted % 3 == 0) {
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX - 0.00106999111, mc.thePlayer.posY + 0.00123087000444446, mc.thePlayer.posZ + 0.00000206999111,false))
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX + 0.00106999111, mc.thePlayer.posY - 0.00123087000444446, mc.thePlayer.posZ - 0.00000206999111,false))
                    }
                }
                "watchdog" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x7, y7 + 0.11, z7, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x7, y7 + 0.1100013579, z7, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x7, y7 + 0.0000013579, z7, false))
                    mc.thePlayer.onCriticalHit(entity)
                }
                "newverus" -> {
                    counter++
                    if (counter == 1) {
                        sendCriticalPacket(yOffset = 0.001, ground = true)
                        sendCriticalPacket(ground = false)
                    }
                    if (counter >= 5)
                        counter = 0
                }
                "randompacket" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(x7, y7 + RandomUtils.nextDouble(minRandomPacketValue.get().toDouble(), maxRandomPacketValue.get().toDouble()), z7, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(x7, y7,z7, false))
                }


                "newpacket" -> {
                    sendCriticalPacket(yOffset = 0.05250000001304, ground = true)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.01400000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    mc.thePlayer.onCriticalHit(entity)
                }

                "mipacket" -> {
                    sendCriticalPacket(yOffset = 0.0625, ground = false)
                    sendCriticalPacket(ground = false)
                }

                "ncp" -> {
                    sendCriticalPacket(yOffset = 0.11, ground = false)
                    sendCriticalPacket(yOffset = 0.1100013579, ground = false)
                    sendCriticalPacket(yOffset = 0.0000013579, ground = false)
                }

                "miniphase" -> {
                    sendCriticalPacket(yOffset = -0.0125, ground = false)
                    sendCriticalPacket(yOffset = 0.01275, ground = false)
                    sendCriticalPacket(yOffset = 0.00025, ground = true)
                }

                "nanopacket" -> {
                    sendCriticalPacket(yOffset = 0.00973333333333, ground = false)
                    sendCriticalPacket(yOffset = 0.001, ground = false)
                    sendCriticalPacket(yOffset = -0.01200000000007, ground = false)
                    sendCriticalPacket(yOffset = -0.0005, ground = false)
                }


                "hypixel" -> {
                    if (lastY - lastY.toInt() == 0.0 || !postValue.get()) {
                        val edit = doubleArrayOf(
                            0.075 + ThreadLocalRandom.current()
                                .nextDouble(0.008) * if (Random.nextBoolean()) 0.96 else 0.97 + mc.thePlayer.ticksExisted % 0.0215 * 0.92,
                            (if (Random.nextBoolean()) 0.010634691223 else 0.013999777) * (if (Random.nextBoolean()) 0.95 else 0.96) * y1[Random.nextInt(
                                y1.size
                            )] * 9.5f
                        )
                        val p = mc.thePlayer
                        if (!postValue.get()) {
                            sendPacketNoEvent(
                                C04PacketPlayerPosition(
                                    p.posX,
                                    p.posY,
                                    p.posZ,
                                    false
                                )
                            )
                        }
                        for (offset in edit) {
                            sendPacketNoEvent(
                                C04PacketPlayerPosition(
                                    p.posX,
                                    p.posY + offset * (1 + MathUtils.getRandomDoubleInRange(-0.005, 0.005)),
                                    p.posZ,
                                    false
                                )
                            )
                        }
                    }
                }



                "tphop" -> {
                    sendCriticalPacket(yOffset = 0.02, ground = false)
                    sendCriticalPacket(yOffset = 0.01, ground = false)
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ)
                }

                "minemora" -> {
                    sendCriticalPacket(yOffset = 0.0114514, ground = false)
                    sendCriticalPacket(yOffset = 0.0010999999940395355, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.0012016413, ground = false)
                }
                "aacpacket" -> {
                    sendCriticalPacket(yOffset = 0.05250000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = true)
                    sendCriticalPacket(yOffset = 0.01400000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)

                }
                "ncppacket" -> {
                    sendCriticalPacket(yOffset = 0.11, ground = false)
                    sendCriticalPacket(yOffset = 0.1100013579, ground = false)
                    sendCriticalPacket(yOffset = 0.0000013579, ground = false)

                }
                "hytnew" -> {
                    mc.thePlayer.motionY = 0.0215
                    sendCriticalPacket(yOffset = 0.042487, ground = false)
                    sendCriticalPacket(yOffset = 0.05250000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.02324649713461, ground = false)
                    sendCriticalPacket(yOffset = 0.0014749900000101, ground = false)

                }
                "fakecollide" -> {
                    val motionX: Double
                    val motionZ: Double
                    if (MovementUtils.isMoving()) {
                        motionX = mc.thePlayer.motionX
                        motionZ = mc.thePlayer.motionZ
                    } else {
                        motionX = 0.00
                        motionZ = 0.00
                    }
                    mc.thePlayer.triggerAchievement(StatList.jumpStat)
                    sendCriticalPacket(xOffset = motionX / 3, yOffset = 0.20000004768372, zOffset = motionZ / 3, ground = false)
                    sendCriticalPacket(xOffset = motionX / 1.5, yOffset = 0.12160004615784, zOffset = motionZ / 1.5, ground = false)
                }


                "motion" -> {
                    when (motionValue.get().lowercase()) {

                        "jump" -> mc.thePlayer.motionY = 0.42
                        "lowjump" -> mc.thePlayer.motionY = 0.3425
                        "hop" -> {
                            mc.thePlayer.motionY = 0.1
                            mc.thePlayer.fallDistance = 0.1f
                            mc.thePlayer.onGround = false
                        }
                    "custom" -> {if (motionYValue.get() != 0f) {
                        mc.thePlayer.motionY = motionYValue.get().toDouble()

                    }
                        mc.timer.timerSpeed = if (mc.thePlayer.motionY > 0) {
                            upTimerValue.get()
                        } else {
                            downTimerValue.get()
                        }
                    }
                    }
                }
            }
            msTimer.reset()
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer) {
            when (modeValue.get().lowercase()) {
                "noground" -> packet.onGround = false
            }

            lastY = packet.positionY
        }
        if (packet is S0BPacketAnimation && debugValue.get()) {
            if (packet.animationType == 4 && packet.entityID == target) {
                alert("DID CRIT")
            }
        }
    }

    override val tag: String
        get() = "${modeValue.get()}-${delayValue.get()}-${packetValue.get()}"
}