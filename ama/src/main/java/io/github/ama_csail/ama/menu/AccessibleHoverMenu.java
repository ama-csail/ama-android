package io.github.ama_csail.ama.menu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.ama_csail.ama.menu.modules.GlossaryModule;
import io.github.ama_csail.ama.menu.modules.MenuModuleType;
import io.github.ama_csail.ama.menu.modules.ModuleLoader;
import io.mattcarroll.hover.Content;
import io.mattcarroll.hover.HoverMenu;

/**
 * The action menu view to be displayed for the accessible menu.
 * @author Aaron Vontell
 */
public class AccessibleHoverMenu extends HoverMenu {

    public static String IDENTIFIER = "AMAAccessibleHoverMenu";
    private List<SectionId> identifiers;

    // The HoverMenu has a set of Sections, which have contents which house the actual models
    // and controllers for the functionality of each menu module. This means that there is an
    // awful cascading of information from the front activity to this menu. This makes encapsulation
    // good, by extendability bad, so in the future we may want to find a solution to this.
    // For now, we will expose methods which allows the menu service to see each module content
    // TODO: Improve the extendability of the menu module system
    private List<Section> modules;
    private Map<MenuModuleType, Section> typeMap; // TODO: Use just the map and no List of modules, but I need to figure out the index step
    private Context context;

    protected AccessibleHoverMenu(Context context) {
        this.identifiers = new LinkedList<>();
        this.modules = new LinkedList<>();
        this.typeMap = new HashMap<>();
        this.context = context;
    }

    protected void registerModule(MenuModuleType type) {

        Section section = ModuleLoader.getModule(this.context, type);
        identifiers.add(section.getId());
        modules.add(section);
        typeMap.put(type, section);
    }

    private void refreshSections() {

        for(Section section : modules) {
            // Note: If your code fails on this line below, it most likely means
            // that you custom module does not implement MenuModule!
            // Furthermore, you should only refresh the contents if the content
            // has been dirtied!
            ((MenuModule) section.getContent()).refreshContents();
        }

    }

    @Override
    public String getId() {
        return IDENTIFIER;
    }

    @Override
    public int getSectionCount() {
        return modules.size();
    }

    @Nullable
    @Override
    public Section getSection(int index) {
        return modules.get(index);
    }

    @Nullable
    @Override
    public Section getSection(@NonNull SectionId sectionId) {
        int index = identifiers.indexOf(sectionId);
        return index > -1 ? modules.get(index) : null;
    }

    @NonNull
    @Override
    public List<Section> getSections() {
        return modules;
    }


    // ---------------------------------------------------------------------------------------------
    //   Module Bindings
    //      This is where you basically create the API for how the accessible menu service will
    //      communicate with the menu modules themselves. You should almost always call for a
    //      refresh when done
    // ---------------------------------------------------------------------------------------------

    public void provideGlossary(Map<String, String> glossary) {
        GlossaryModule mod = (GlossaryModule) typeMap.get(MenuModuleType.GLOSSARY).getContent();
        mod.setGlossary(glossary);
        refreshSections();
    }

}
