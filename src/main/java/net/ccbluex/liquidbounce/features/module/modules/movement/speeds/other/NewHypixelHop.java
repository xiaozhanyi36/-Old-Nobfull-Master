/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;

public class NewHypixelHop extends SpeedMode {
    private boolean legitJump;
    private final MSTimer timer=new MSTimer();
    public NewHypixelHop() {
        super("HypixelHop");
    }


    @Override
    public void onEnable() {
    }

    public void onMotion() {
    }
    private boolean stage = false;

    @Override
    public void onUpdate() {
        if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null) {
            final Speed speed = (Speed) LiquidBounce.moduleManager.getModule(Speed.class);
            if (speed == null)
                return;

            if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null) {
                if (MovementUtils.isMoving()) {
                    mc.gameSettings.keyBindJump.pressed = false;
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        mc.thePlayer.speedInAir = 0.02F;
                        MovementUtils.strafe(0.43F);
                    }
                    if (stage) {
                        if (mc.thePlayer.fallDistance <= 0.1) {
                            mc.timer.timerSpeed = speed.getHypixelTimerValue().get();
                        }
                        if (timer.hasTimePassed(650)) {
                            timer.reset();
                            stage = !stage;
                        }
                    } else {
                        if (mc.thePlayer.fallDistance > 0.1 && mc.thePlayer.fallDistance < 1.3) {
                            mc.timer.timerSpeed = speed.getHypixelDealyTimerValue().get();
                        }
                        if (timer.hasTimePassed(400)) {
                            timer.reset();
                            stage = !stage;
                        }
                    }
                    MovementUtils.strafe();
                }

            }
        }
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}