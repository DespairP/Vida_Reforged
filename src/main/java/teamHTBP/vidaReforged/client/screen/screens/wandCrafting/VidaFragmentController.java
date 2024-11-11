package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiGraphics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.client.screen.components.VidaLifecycleSection;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class VidaFragmentController {
    private Set<VidaLifecycleSection> registeredSection = new HashSet<>();

    private LinkedList<VidaLifecycleSection> currentSections = null;

    private Logger LOGGER = LogManager.getLogger();

    public VidaFragmentController(LinkedList<VidaLifecycleSection> sections){
        this.currentSections = new LinkedList<>();
        registerSections(sections);
    }

    private void registerSections(LinkedList<VidaLifecycleSection> sections){
        registeredSection.addAll(sections);
        registeredSection.forEach(section -> {
            section.init();
            section.setVisible(false);
        });
        push(sections.get(0));
    }

    public void push(VidaLifecycleSection section){
        // 检查是否注册过
        if(!registeredSection.contains(section)){
            LOGGER.error("section {} is not registered", section.getClass().getName());
            return;
        }
        //
        if(currentSections.size() > 0){
            VidaLifecycleSection oldSection = getCurrent();
            oldSection.hide();
            oldSection.setVisible(false);
        }
        currentSections.remove(section);
        currentSections.push(section);
        section.setVisible(true);
        section.start();
    }

    public void popSection(){
        if(currentSections.size() == 0){
            return;
        }
        VidaLifecycleSection oldSection = currentSections.pop();
        oldSection.setVisible(false);
        oldSection.hide();
        if(!currentSections.isEmpty()){
            VidaLifecycleSection newSection = getCurrent();
            newSection.setVisible(true);
            newSection.resume();
        }
    }

    public VidaLifecycleSection getCurrent(){
        return this.currentSections.getFirst();
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        this.currentSections.getLast().render(graphics, mouseX, mouseY, partialTicks);
    }

    public List<VidaLifecycleSection> getSections(){
        return ImmutableList.copyOf(currentSections);
    }
}
