package actors;

import actor.messages.Messages;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.AskTimeoutException;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.gracefulStop;

/**
 * Main class for your wordcount actor system.
 *
 * @author
 */
public class WordCount {

    public static void main(String[] args) throws Throwable {
        ActorSystem system = ActorSystem.create("wordcounter");
        ;
        Props wordCountUser = Props.create(WordCountActor.class);
        ActorRef actorRef = system.actorOf(wordCountUser);

        Timeout timeout = new Timeout(Duration.create(20, "seconds"));
        Future<Object> future = Patterns.ask(actorRef, "StartProcessingFolder", timeout);
        Patterns.pipe(future, system.dispatcher()).to(actorRef);

        try {
            Future<Boolean> stopped =
                    gracefulStop(actorRef, Duration.create(20, TimeUnit.SECONDS), WordCountActor.SHUTDOWN);
            Await.result(stopped, Duration.create(22, TimeUnit.SECONDS));
            Future<Boolean> stopped1 =
                    gracefulStop(actorRef, Duration.create(20, TimeUnit.SECONDS), WordCountInAFileActor.SHUTDOWN);
            Await.result(stopped, Duration.create(22, TimeUnit.SECONDS));
        } catch (AskTimeoutException e) {
            // the actor wasn't stopped within 5 seconds
        }

        // Messages.FinalResultResponse result = (Messages.FinalResultResponse) Await.result(future, timeout.duration());

            /*
         * Create the WordCountActor and send it the StartProcessingFolder
		 * message. Once you get back the response, use it to print the result.
		 * Remember, there is only one actor directly under the ActorSystem.
		 * Also, do not forget to shutdown the actorsystem
		 */
        system.terminate();
    }


}

