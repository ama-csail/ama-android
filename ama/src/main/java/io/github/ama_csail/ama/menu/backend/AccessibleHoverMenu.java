package io.github.ama_csail.ama.menu.backend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

import io.github.ama_csail.ama.menu.modules.GlossarySection;
import io.mattcarroll.hover.HoverMenu;

/**
 * The action menu view to be displayed for the accessible menu.
 * @author Aaron Vontell
 */
public class AccessibleHoverMenu extends HoverMenu {

    public static String IDENTIFIER = "AMAAccessibleHoverMenu";
    private List<SectionId> identifiers;
    private List<Section> modules;
    private Context context;

    public AccessibleHoverMenu(Context context) {
        this.identifiers = new LinkedList<>();
        this.modules = new LinkedList<>();
        this.context = context;
    }

    public void registerModule() {
        Section glossary = GlossarySection.getGlossarySection(this.context);
        identifiers.add(glossary.getId());
        modules.add(glossary);
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
        return modules.get(index);
    }

    @NonNull
    @Override
    public List<Section> getSections() {
        return modules;
    }
}
