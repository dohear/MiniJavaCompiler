class RequestHandler {
    public static void main(String[] args) {
        int statusCode = 200;
        int requestCount = 0;
        int maxRequests = 5;

        Xinu.print("Server starting...");
        Xinu.println();

        while (requestCount < maxRequests) {
            Xinu.print("Processing request #");
            Xinu.printint(requestCount + 1);
            Xinu.println();

            if (requestCount == 3) {
                statusCode = 500;
                Xinu.print("ERROR: Internal server error");
                Xinu.println();
            } else {
                statusCode = 200;
                Xinu.print("Status: OK");
                Xinu.println();
            }

            requestCount = requestCount + 1;
        }

        Xinu.print("Server shutdown. Total requests: ");
        Xinu.printint(requestCount);
        Xinu.println();
    }
}
