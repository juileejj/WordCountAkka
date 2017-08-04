package actors;

import java.io.File;

import actor.messages.Messages;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinGroup;
import akka.routing.RoundRobinPool;

/**
 * This is the main actor and the only actor that is created directly under the
 * {@code ActorSystem} This actor creates more child actors
 * {@code WordCountInAFileActor} depending upon the number of files in the given
 * directory structure
 *
 * @author
 */
public class WordCountActor extends UntypedActor {

    private Long count = new Long(0);
    public static final String SHUTDOWN = "shutdown";

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof String) {
            String request = (String) msg;
            System.out.println(request);
            File folder = new File("src/main/resources/input_data");
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles.length == 0) {
                System.out.println("The directory is empty");
            } else {
                for (int i = 0; i < listOfFiles.length; i++) {
                    getContext().actorOf(Props.create(WordCountInAFileActor.class)).tell(listOfFiles[i], getSelf());
                }
            }
        } else if (msg instanceof Messages.FileResultResponse) {
            System.out.println("In required :");
            Messages.FileResultResponse resultResponse = (Messages.FileResultResponse) msg;
            long fileCount = resultResponse.getCount();
            count += fileCount;
            final Messages.FinalResultResponse finalResultResponse = new Messages.FinalResultResponse(count);
            System.out.println("WordCountActor: " + finalResultResponse.getCount());
        } else {
            unhandled(msg);
        }
    }

}
