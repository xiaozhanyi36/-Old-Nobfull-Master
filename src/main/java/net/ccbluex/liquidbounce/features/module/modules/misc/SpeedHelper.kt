package net.ccbluex.liquidbounce.features.module.modules.misc


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.features.module.modules.movement.TargetStrafe
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "SpeedHelper", description = "LOL",
    category = ModuleCategory.MISC)
class SpeedHelper : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Jump", "Motion", "SpeedModule"), "Jump")
    private val lowjumpspeedValue = FloatValue("LowJumpSpeed", 0.6f, 0.01f, 10f)
    private val motionValue = FloatValue("MotionY", 0.4f, 0.01f, 0.5f)
    private val targetstrafeValue = BoolValue("TargetStrafe", false)

    @EventTarget
    fun onAttack(event: AttackEvent) {
        val target = event.targetEntity

        if (mc.thePlayer.isInLava && mc.thePlayer.isBurning)
            return

        if (mc.thePlayer.hurtTime > 0) {

        if (target != null && !LiquidBounce.moduleManager.getModule(TargetStrafe::class.java)!!.state &&targetstrafeValue.get()) {
            LiquidBounce.moduleManager.getModule(TargetStrafe::class.java)!!.toggle()
        }

            when(modeValue.get().toLowerCase()) {
                "jump" -> {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump()
                    }
                }
                "motion" -> {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = motionValue.get().toDouble()
                    }
                }
                "speedmodule" -> if(!LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state) {
                    LiquidBounce.moduleManager.getModule(Speed::class.java)!!.toggle()
                }
            }
        } else {
            if (LiquidBounce.moduleManager.getModule(Speed::class.java)!!.state) {
                LiquidBounce.moduleManager.getModule(Speed::class.java)!!.toggle()
            }
            if (LiquidBounce.moduleManager.getModule(TargetStrafe::class.java)!!.state) {
                LiquidBounce.moduleManager.getModule(TargetStrafe::class.java)!!.toggle()
            }

        }
    }


    override val tag: String?
        get() = modeValue.get()

}