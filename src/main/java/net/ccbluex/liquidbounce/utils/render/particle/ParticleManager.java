package net.ccbluex.liquidbounce.utils.render.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ParticleManager {
    public ArrayList particles = new ArrayList();
    private final Particle particle;
    private final int amount;
    private final Random random = new Random();

    public ParticleManager(Particle particle, int amount) {
        this.particle = particle;
        this.amount = amount;
        this.init();
    }

    private void init() {
        this.particles.clear();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        for (int i = 0; i < this.amount; ++i) {
            ParticleSnow particle = new ParticleSnow();
            if (particle instanceof ParticleSnow) {
                particle = new ParticleSnow();
            }

            particle.vector.x = (float) this.random.nextInt(res.getScaledWidth() + 1);
            particle.vector.y = (float) this.random.nextInt(res.getScaledHeight() + 1);
            this.particles.add(particle);
        }

    }

    public void draw(int xAdd) {
        Iterator var3 = this.particles.iterator();

        while (var3.hasNext()) {
            Particle particle = (Particle) var3.next();
            particle.draw(xAdd);
        }

    }
}
