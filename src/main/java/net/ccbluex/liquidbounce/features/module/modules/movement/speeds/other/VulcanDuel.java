package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class VulcanDuel extends SpeedMode {

    public void onUpdate() {}

    public void onMove(MoveEvent moveevent) {}

    public void onPreMotion() {
        if (MovementUtils.isMoving() && !VulcanDuel.mc.thePlayer.movementInput.jump) {
            if (VulcanDuel.mc.thePlayer.onGround) {
                MovementUtils.strafe(1.1F);
                VulcanDuel.mc.thePlayer.motionY = 0.15D;
            } else {
                MovementUtils.strafe();
            }
        }
    }

    public VulcanDuel() {
        super("VulcanDuel");
    }

    public void onMotion() {}
}
