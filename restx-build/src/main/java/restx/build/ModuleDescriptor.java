package restx.build;

import java.util.*;

/**
 * User: xavierhanin
 * Date: 4/14/13
 * Time: 2:07 PM
 */
public class ModuleDescriptor {
    private final GAV parent;
    private final GAV gav;
    private final String packaging;
    private final Map<String,String> properties;
    private final List<String> propertiesFileReferences;
    private final Map<String, List<ModuleFragment>> fragments;
    private final Map<String, List<ModuleDependency>> dependencies;
    private final ModuleDescriptor parsedModuleDescriptor;

    public ModuleDescriptor(GAV parent, GAV gav, String packaging,
                            Map<String, String> properties,
                            List<String> propertiesFileReferences,
                            Map<String, List<ModuleFragment>> fragments,
                            Map<String, List<ModuleDependency>> dependencies,
                            ModuleDescriptor parsedModuleDescriptor) {
        this.parent = parent;
        this.gav = gav;
        this.packaging = packaging;
        this.propertiesFileReferences = propertiesFileReferences;
        this.fragments = Collections.unmodifiableMap(fragments);
        this.properties = Collections.unmodifiableMap(properties);
        this.dependencies = Collections.unmodifiableMap(dependencies);
        this.parsedModuleDescriptor = parsedModuleDescriptor;
    }

    public GAV getParent() {
        return parent;
    }

    public GAV getGav() {
        return gav;
    }

    public String getPackaging() {
        return packaging;
    }

    public Map<String,String> getProperties() {
        return properties;
    }

    public Set<String> getDependencyScopes() {
        return dependencies.keySet();
    }

    public List<ModuleDependency> getDependencies(String scope) {
        return dependencies.get(scope);
    }

    public Set<String> getFragmentTypes() {
        return fragments.keySet();
    }

    public List<ModuleFragment> getFragments(String s) {
        List<ModuleFragment> moduleFragments = fragments.get(s);
        return moduleFragments == null ? Collections.<ModuleFragment>emptyList() : moduleFragments;
    }

    public ModuleDescriptor concatDependency(String scope, ModuleDependency dep) {
        LinkedHashMap<String, List<ModuleDependency>> newDeps = new LinkedHashMap<>(dependencies);
        if (!newDeps.containsKey(scope)) {
            newDeps.put(scope, new ArrayList<ModuleDependency>());
        } else {
            newDeps.put(scope, new ArrayList<ModuleDependency>(newDeps.get(scope)));
        }
        newDeps.get(scope).add(dep);

        return new ModuleDescriptor(parent, gav, packaging, properties, propertiesFileReferences, fragments, newDeps, parsedModuleDescriptor);
    }
    
    public boolean hasClassifier() {
        for (String scope : this.getDependencyScopes()) {
            for (ModuleDependency dependency : this.getDependencies(scope)) {
                if (dependency.getGav().getClassifier()!=null) {
                	return true;
                }
            }
        }
        return false;
    }

    public ModuleDescriptor getParsedModuleDescriptor() {
        return parsedModuleDescriptor==null?this:parsedModuleDescriptor;
    }

    public List<String> getPropertiesFileReferences() {
        return propertiesFileReferences;
    }
}
