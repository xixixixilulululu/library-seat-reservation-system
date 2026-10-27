import java.util.HashSet;
import java.util.Set;

public class Student {
    private String studentId;
    private String name;
    private Set<String> preferences;  // Preference sets: such as "window", "power_outlet", "quiet", etc

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.preferences = new HashSet<>();
    }

//Add preferences

    public void addPreference(String preference) {
        if (preference != null && !preference.isBlank()) {
            preferences.add(preference.toLowerCase());
        }
    }

//Remove preferences"

    public void removePreference(String preference) {
        if (preference != null) {
            preferences.remove(preference.toLowerCase());
        }
    }

//Check if there is any preference

    public boolean hasPreference(String preference) {
        return preference != null && preferences.contains(preference.toLowerCase());
    }

// Get all preferences

    public Set<String> getPreferences() {
        return new HashSet<>(preferences);
    }

//Set multiple preferences

    public void setPreferences(String...  prefs) {
        preferences.clear();
        for (String pref : prefs) {
            addPreference(pref);
        }
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Student [ID: %s, name: %s, preferences: %s]", studentId, name, preferences);
    }
}
