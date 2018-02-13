public enum Ports {
    CLIENT(8080),
    WORKER(8081);

    private int port;

    Ports(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}
