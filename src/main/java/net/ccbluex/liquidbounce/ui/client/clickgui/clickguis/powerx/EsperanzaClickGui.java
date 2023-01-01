
package net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.powerx;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.clickgui.clickguis.guis.Opacity;
import net.ccbluex.liquidbounce.utils.render.RenderUtil;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * @author Qingjiu SKID
 * QWQ 别删掉这一段 有bug自己修 091221
 */
public class EsperanzaClickGui extends GuiScreen implements GuiYesNoCallback {
	public static ModuleCategory currentModuleType = ModuleCategory.COMBAT;
	public static Module currentModule = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size() != 0
			? LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(0)
			: null;
	public static float startX = 100, startY = 100;
	public int moduleStart = 0;
	public int valueStart = 0;
	boolean previousmouse = true;
	boolean mouse;
	public Opacity opacity = new Opacity(0);
	public int opacityx = 255;
	public float moveX = 0, moveY = 0;
	GameFontRenderer dfo = Fonts.font35;
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
	
		if (isHovered(startX, startY - 25, startX + 400, startY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
			if (moveX == 0 && moveY == 0) {
				moveX = mouseX - startX;
				moveY = mouseY - startY;
			} else {
				startX = mouseX - moveX;
				startY = mouseY - moveY;
			}
			this.previousmouse = true;
		} else if (moveX != 0 || moveY != 0) {
			moveX = 0;
			moveY = 0;
		}
		this.opacity.interpolate((float) opacityx);
	
	
		  RenderUtil.drawRoundedRect(this.startX - 1, this.startY - 1, this.startX + 370 + 1, this.startY + 230 + 1, (int) 3F,new Color(11, 84, 150,150).getRGB());
	        RenderUtil.drawRoundedRect(this.startX, this.startY, this.startX + 370, this.startY + 230, (int) 2F, new Color(27, 27, 27).getRGB());
	        RenderUtil.drawRect(this.startX, this.startY+20.5, this.startX + 370, this.startY + 20, new Color(21, 21, 21,140).getRGB());
	        RenderUtil.drawRect(this.startX, this.startY+23, this.startX + 370, this.startY + 20, new Color(23, 23, 23,130).getRGB());
	        RenderUtil.drawRect(this.startX+80, this.startY+20.3, this.startX + 180, this.startY + 230, new Color(27, 27, 27).getRGB());
	        RenderUtil.drawRect(this.startX+80, this.startY+20.3, this.startX + 80.8, this.startY + 230, new Color(12, 12, 12,200).getRGB());
	        RenderUtil.drawRect(this.startX+80.8, this.startY+20.3, this.startX + 81.6, this.startY + 230, new Color(13, 13, 13,200).getRGB());
	        RenderUtil.drawRect(this.startX+81.6, this.startY+20.3, this.startX + 82.4, this.startY + 230, new Color(14, 14, 14,200).getRGB());
	        RenderUtil.drawRect(this.startX+82.4, this.startY+20.3, this.startX + 83.2, this.startY + 230, new Color(15, 15, 15,200).getRGB());
	        RenderUtil.drawRect(this.startX+83.2, this.startY+20.3, this.startX + 84, this.startY + 230, new Color(16, 16, 16,200).getRGB());
	        RenderUtil.drawRect(this.startX+84, this.startY+20.3, this.startX + 84.8, this.startY + 230, new Color(17, 17, 17,200).getRGB());
	        RenderUtil.drawRect(this.startX+84.8, this.startY+20.3, this.startX + 85.8, this.startY + 230, new Color(18, 18, 18,190).getRGB());
	        RenderUtil.drawRect(this.startX+85.8, this.startY+20.3, this.startX + 90, this.startY + 230, new Color(20, 20, 20,110).getRGB());
	        RenderUtil.drawRect(this.startX+185, this.startY+20.3, this.startX + 182, this.startY + 230, new Color(21, 21, 21,110).getRGB());
	        RenderUtil.drawRect(this.startX+182, this.startY+20.3, this.startX + 180, this.startY + 230, new Color(21, 21, 21,100).getRGB());
		Fonts.font35.drawString("LiquidBounce", startX + 20, startY + 5,(new Color(152, 152, 152)).getRGB());
		
		
		for (int i = 0; i < ModuleCategory.values().length; i++) {
			ModuleCategory[] iterator = ModuleCategory.values();
			if (iterator[i] != currentModuleType) {
				
				RenderUtils.circle(startX + 30, startY + 50 + i * 20, 6,new Color(255, 255, 255,0).getRGB());
			} else {
				//RenderUtil.drawBorderedRect(startX+1, startY+1, startX+30, startY+30, 3, new Color(255, 255, 255,100).getRGB(), new Color(255, 255, 255 ,180).getRGB());
				//RenderUtils.circle(startX + 30, startY + 50 + i * 20, 6,new Color(255, 255, 250, (int) opacity.getOpacity()).getRGB());
			}
			try {
				if (this.isCategoryHovered(startX + 15, startY + 40 + i * 20, startX + 60, startY + 50 + i * 20, mouseX,
						mouseY) && Mouse.isButtonDown((int) 0)) {
					currentModuleType = iterator[i];
					currentModule = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size() != 0
							? LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(0)
							: null;
					moduleStart = 0;
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		int m = Mouse.getDWheel();
		if (this.isCategoryHovered(startX + 60, startY, startX + 200, startY + 320, mouseX, mouseY)) {
			if (m < 0 && moduleStart < LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size() - 1) {
				moduleStart++;
			}
			if (m > 0 && moduleStart > 0) {
				moduleStart--;
			}
		}
		if (this.isCategoryHovered(startX + 200, startY, startX + 420, startY + 320, mouseX, mouseY)) {
			if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
				valueStart++;
			}
			if (m > 0 && valueStart > 0) {
				valueStart--;
			}
		}
		dfo.drawString(
				currentModule == null ? currentModuleType.toString()
						: currentModuleType.toString() ,
				startX + 85, startY + 5, new Color(152,152,152).getRGB());
		dfo.drawString(
				currentModule == null ? currentModuleType.toString()
						: "Module:"+ currentModule.getName(),
				startX + 185, startY + 5, new Color(152,152,152).getRGB());
		if (currentModule != null) {
			float mY = startY + 16;
			for (int i = 0; i < LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size(); i++) {
				Module module = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(i);
				if (mY > startY + 200)
					break;
				if (i < moduleStart) {
					continue;
				}
				RenderUtil.drawRoundedRect(startX +88, mY+8 , startX + 175, mY + 25, (int) 8F, new Color(39, 39, 39).getRGB());
				dfo.drawString(module.getName(), startX + 108, mY + 11,
						new Color(68,68,68, (int) opacity.getOpacity()).getRGB());
				RenderUtils.circle(startX +95, mY+16 , 2, new Color(152,152,152).getRGB());
				if (module.getState()) {
					RenderUtil.drawRoundedRect(startX +88, mY+8 , startX + 175, mY + 25, (int) 8F, new Color(58, 58, 58).getRGB());
					dfo.drawString(module.getName(), startX + 108, mY + 11,
							new Color(152,152,152, (int) opacity.getOpacity()).getRGB());
					RenderUtils.circle(startX +95, mY+16 , 2, new Color(0, 100, 237).getRGB());
				}
			
				if(module.getValues().size()>0) {
					dfo.drawString("+", startX +167, mY+10 , new Color(150,150,150).getRGB());
	    		}
				if (isSettingsButtonHovered(startX + 108, mY,
						startX + 100 + (dfo.getStringWidth(module.getName())),
						mY + 12 + dfo.getStringWidth("1"), mouseX, mouseY)) {
					if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
						if (module.getState()) {
							module.setState(false);
						} else {
							module.setState(true);
						}
						previousmouse = true;
					}
					if (!this.previousmouse && Mouse.isButtonDown((int) 1)) {
						previousmouse = true;
					}
				}

				if (!Mouse.isButtonDown((int) 0)) {
					this.previousmouse = false;
				}
				if (isSettingsButtonHovered(startX + 90, mY,
						startX + 100 + (dfo.getStringWidth(module.getName())),
						mY + 12 + dfo.getStringWidth("1"), mouseX, mouseY) && Mouse.isButtonDown((int) 1)) {
					currentModule = module;
					valueStart = 0;
				}
				mY += 18;
			}
			mY = startY + 25;
			for (int i = 0; i < currentModule.getValues().size(); i++) {
				if (mY > startY + 215)
					break;
				if (i < valueStart) {
					continue;
				}
				GameFontRenderer font = Fonts.font35;
				Value value = currentModule.getValues().get(i);
				if (value instanceof FloatValue) {
					float x = startX + 290;
					double render = (double) (68.0F
							* (((Number) value.getValue()).floatValue() - ((FloatValue) value).getMinimum())
							/ (((FloatValue) value).getMaximum()
							- ((FloatValue) value).getMinimum()));
					RenderUtil.drawRect((float) x - 6, mY + 2, (float) ((double) x + 74), mY + 3,
							(new Color(200, 200, 200, (int) opacity.getOpacity())).getRGB());
					RenderUtil.drawRect((float) x - 6, mY + 2, (float) ((double) x + render + 6.5D), mY + 3,
							(new Color(0, 100, 237, (int) opacity.getOpacity())).getRGB());
					RenderUtils.circle((float)(x + render + 4), (float)mY+2, 2F, new Color(9, 157, 227).getRGB());
					//RenderUtil.drawCircle((float) ((double) x + render + 2D), mY, 10,new Color(161, 141, 255).getRGB());
					//RenderUtil.drawRect((float) ((double) x + render + 2D), mY, (float) ((double) x + render + 7D),
					//	mY + 5, (new Color(61, 141, 255, (int) opacity.getOpacity())).getRGB());
					font.drawString(value.getName() + ": " + value.getValue(), startX + 190, mY, new Color(152,152,152).getRGB());
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
					}
					if (this.isButtonHovered(x, mY - 2, x + 100, mY + 7, mouseX, mouseY)
							&& Mouse.isButtonDown((int) 0)) {
						if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
							render = ((FloatValue) value).getMinimum();
							double max = ((FloatValue) value).getMaximum();
							double inc = 0.01;
							double valAbs = (double) mouseX - ((double) x + 1.0D);
							double perc = valAbs / 68.0D;
							perc = Math.min(Math.max(0.0D, perc), 1.0D);
							double valRel = (max - render) * perc;
							double val = render + valRel;
							val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
							((FloatValue) value).setValue((float)val);
						}
						if (!Mouse.isButtonDown((int) 0)) {
							this.previousmouse = false;
						}
					}
					mY += 20;
				}

				if (value instanceof BoolValue) {
					float x = startX + 230;
					font.drawString(value.getName(), startX + 190, mY, new Color(152,152,152).getRGB());
					//RenderUtil.drawRect(x + 56, mY , x + 76, mY + 9,
						//	new Color(240, 240, 240, (int) opacity.getOpacity()).getRGB());
					if ((boolean) value.getValue()) {
						RenderUtil.drawRoundedRect(x + 56, mY-1 , x + 76, mY + 9, (int) 4F, new Color(58, 58, 58).getRGB());
						//RenderUtil.drawRect(x + 56, mY-1 , x + 76, mY + 9,
							//	new Color(220, 220, 255, (int) opacity.getOpacity()).getRGB());
						RenderUtils.circle(x+72, mY+4, 4, new Color(0, 100, 237).getRGB());
					} else {
						
						RenderUtil.drawRoundedRect(x + 56, mY-1 , x + 76, mY + 9, (int) 4F, new Color(58, 58, 58).getRGB());
						RenderUtils.circle(x+60, mY+4, 4, new Color(152,152,152).getRGB());
					}
					if (this.isCheckBoxHovered(x + 56, mY, x + 76, mY + 9, mouseX, mouseY)) {
						if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
							this.previousmouse = true;
							this.mouse = true;
						}

						if (this.mouse) {
							value.setValue(!(boolean) value.getValue());
							this.mouse = false;
						}
					}
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
					}
					mY += 20;
				}
				if (value instanceof ListValue) {
					float x = startX + 275;
					Fonts.font35.drawStringWithShadow(value.getName(), startX + 190, mY, new Color(152, 152, 152, (int) opacity.getOpacity()).getRGB() );

					RenderUtil.drawRect(x +20, mY, x + 65, mY + 10,
							new Color(120, 120, 120, (int) opacity.getOpacity()).getRGB() );
					RenderUtil.drawRect(x+5, mY, x+15, mY + 10,
							new Color(0, 100, 237, (int) opacity.getOpacity()).getRGB() );
					RenderUtil.drawRect(x+70, mY, x+80, mY + 10,
							new Color(0, 100, 237, (int) opacity.getOpacity()).getRGB() );
					Fonts.font35.drawStringWithShadow("<",x +8, mY, new Color(152, 152, 152, (int) opacity.getOpacity()).getRGB() );
					Fonts.font35.drawStringWithShadow(">",x +72, mY, new Color(152, 152, 152, (int) opacity.getOpacity()).getRGB() );
					Fonts.font35.drawStringWithShadow(((ListValue) value).getValue(),
							(float) (x + 40 - 	Fonts.font35.getStringWidth(((ListValue) value).getValue()) / 2), mY, -1);
					if (this.isStringHovered(x, mY - 5, x + 100, mY + 15, mouseX, mouseY)) {
						if (Mouse.isButtonDown((int) 0) && !this.previousmouse) {
							ListValue listValue = (ListValue)value;
							String current = (String)listValue.get();
							value.set((Object)listValue.getValues()[listValue.getModeListNumber(current) + 1 >= listValue.getValues().length ? 0 : listValue.getModeListNumber(current) + 1]);
							this.previousmouse = true;
						}
						if (!Mouse.isButtonDown((int) 0)) {
							this.previousmouse = false;
						}

					}
					mY += 25;
				}

			     
				}
			dfo.drawString("Visual", startX + 25, startY + 60,(new Color(152, 152, 152)).getRGB());
			dfo.drawString("Fight", startX + 25, startY + 40,(new Color(152, 152, 152)).getRGB());
			dfo.drawString("Move", startX + 25, startY + 80,(new Color(152, 152, 152)).getRGB());
			dfo.drawString("Player", startX + 25, startY + 100,(new Color(0, 100, 237)).getRGB());
			dfo.drawString("Misc", startX + 25, startY + 120,(new Color(152, 152, 152)).getRGB());

			RenderUtils.drawImage(new ResourceLocation("MisakaMikoto/Clickui/Combat.png"), (int)startX+5, (int)startY+40,14,14);
			RenderUtils.drawImage(new ResourceLocation("MisakaMikoto/Clickui/Render.png"), (int)startX+5, (int)startY+60,14,14);
			RenderUtils.drawImage(new ResourceLocation("MisakaMikoto/Clickui/Movement.png"), (int)startX+5, (int)startY+80,14,14);
			RenderUtils.drawImage(new ResourceLocation("MisakaMikoto/Clickui/"+currentModuleType+2+".png"), (int)startX+5, (int)startY+100,14,14);
			RenderUtils.drawImage(new ResourceLocation("MisakaMikoto/Clickui/World.png"), (int)startX+5, (int)startY+120,14,14);
		}

	}

	public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	@Override
	public void onGuiClosed() {
		this.opacity.setOpacity(0);
	}
}
