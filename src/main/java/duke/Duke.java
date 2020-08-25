package duke;

import duke.command.Command;
import duke.task.TaskList;

public class Duke {
    protected Storage storage;
    protected TaskList taskList;
    protected Ui ui;

    public Duke(String filePath) {
        this.storage = new Storage(filePath);
        this.ui = new Ui();
        this.taskList = new TaskList(storage.tasks);
    }

    public static void main(String[] args) throws DukeException {
        Duke duke = new Duke("duke.txt");
        // displayed once Duke is run, without input from user
        duke.ui.greetings();
        Parser parser = new Parser(duke);
        boolean isExit = false;

        while (!isExit) {
            String input = duke.ui.readCommand();
            try {
                Command command = parser.parse(input, duke.taskList);
                duke.ui.showLine();
                command.execute();
                isExit = command.isExit;
            } catch (DukeException ex) {
                System.out.println(ex);
            }

            if (!isExit) {
                duke.ui.showLine();
            }
        }

        // Save tasks before terminating program
        duke.storage.saveTasks(duke.taskList.tasks);
        duke.ui.goodbye();
    }
}