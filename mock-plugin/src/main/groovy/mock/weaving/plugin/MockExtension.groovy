package mock.weaving.plugin;

class MockExtension {
    def enabled = true

    def setEnabled(boolean enabled) {
        this.enabled = enabled
    }

    def getEnabled() {
        return enabled;
    }
}
