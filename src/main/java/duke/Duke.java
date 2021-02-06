package duke;

import java.util.List;

import duke.exceptions.DukeException;
import duke.exceptions.UnknownInputException;
import duke.tasks.DeadlineTask;
import duke.tasks.EventTask;
import duke.tasks.Task;
import duke.tasks.ToDoTask;

/**
 * Duke class that simulates the running of the Duke Program
 */
public class Duke{

    /** Storage instance that is used by Duke during run for loading and writing of file*/
    private Storage storage;

    /** TaskList instance used by Duke during run that manages the tasks */
    private TaskList tasks;

    /** Ui instance used by Duke during run to interact with User */
    private Ui ui;

    private String filePath = "./data/tasks.txt";

    /**
     * Constructor for the Duke class
     */
    public Duke() {

        ui = new Ui();

        try {
            storage = new Storage(filePath);
            tasks = new TaskList(storage.load());

        } catch (DukeException e) {

            ui.showLoadingError();
            tasks = new TaskList();

        }
    }
    
    /**
     * You should have your own function to generate a response to user input.
     * Replace this stub with your completed method.
     */
    protected String getResponse(String input) {
        String toReply="";
        try {
                Parser parser = new Parser(input);
                parser.check();
            
                String[] parsedInput = parser.getParsedAction();
                
                switch (parsedInput[0]) {
                case "todo":
                    
                    assert parsedInput.length > 1 : "Something went wrong with the parsing!";
                    toReply += ui.addPrint();
                    ToDoTask todo = tasks.handleToDoTask(input);
                    toReply += ui.printTask(todo);
                    toReply += ui.countTasks(tasks);

                    break;

                case "deadline":
    
                    assert parsedInput.length > 1 : "Something went wrong with the parsing!";
                    toReply += ui.addPrint();

                    DeadlineTask deadlineTask = tasks.handleDeadlineTask(input);
    
                    toReply += ui.printTask(deadlineTask);
                    toReply += ui.countTasks(tasks);

                    break;

                case "event":
    
                    assert parsedInput.length > 1 : "Something went wrong with the parsing!";
                    toReply += ui.addPrint();

                    EventTask eventTask = tasks.handleEventTask(input);
    
                    toReply += ui.printTask(eventTask);
                    toReply += ui.countTasks(tasks);

                    break;

                case "list":
                    toReply += ui.printStored(tasks);

                    break;

                case "done":
    
                    assert parsedInput.length > 1 : "Something went wrong with the parsing!";
                    int number = Integer.valueOf(parsedInput[1]);
    
                    toReply += ui.printMarked();

                    Task completed = tasks.handleDone(number);
    
                    toReply += ui.printTask(completed);

                    break;

                case "check":
    
                    assert parsedInput.length > 1 : "Something went wrong with the parsing!";
                    String result = tasks.findOnDateTasks((parsedInput[1]));
    
                    toReply += ui.print(result);
                    break;

                case "bye":
                    toReply += Ui.printBye();
                    break;

                case "delete":
    
                    assert parsedInput.length > 1 : "Something went wrong with the parsing!";
                    int index = Integer.valueOf(parsedInput[1]);
    
                    toReply += ui.printRemoved();

                    Task task = tasks.handleDelete(index);
    
                    toReply += ui.printTask(task);
                    toReply += ui.countTasks(tasks);

                    break;

                case "find":
    
                    assert parsedInput.length > 1 : "Something went wrong with the parsing!";
                    String keyword = parsedInput[1];
    
                    toReply += ui.printMatching();

                    List<Task> matches = tasks.getMatch(keyword);
    
                    toReply += ui.printList(matches);

                    break;

                default:
                    throw new UnknownInputException();
                }
                
            storage.write(tasks);
        
        } catch (DukeException e) {
            return e.getMessage();
        }
    
        return toReply;
    }
    
}
