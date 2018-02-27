
public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        MainView mainView = new MainView(model, controller);
    }
}
