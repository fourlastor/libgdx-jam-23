package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import io.github.fourlastor.game.level.Match;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.Player;
import io.github.fourlastor.game.level.Round;
import io.github.fourlastor.game.route.Router;

import javax.inject.Inject;

/**
 * Coordinates the movement between each pair of scene2d actor and box2d body.
 * Actors follow the bodies.
 */
public class GoToNextScreenSystem extends EntitySystem implements Telegraph {

    private final MessageDispatcher dispatcher;
    private final Router router;
    private final Match match;

    @Inject
    public GoToNextScreenSystem(
            MessageDispatcher dispatcher,
            Router router,
            Match match) {
        this.dispatcher = dispatcher;
        this.router = router;
        this.match = match;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        dispatcher.addListener(this, Message.PLAYER_DEFEATED.ordinal());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        dispatcher.removeListener(this);
        super.removedFromEngine(engine);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == Message.PLAYER_DEFEATED.ordinal() && msg.extraInfo instanceof Player) {
            Player previousLoser = ((Player) msg.extraInfo);
            if (match.round == Round.FINAL_ROUND || (match.round == Round.ROUND_2 && previousLoser == match.previousLoser)) {
                dispatcher.dispatchMessage(Message.MATCH_END.ordinal(), previousLoser == Player.P1 ? Player.P2 : Player.P1);
            } else {
                Round round = match.round == Round.ROUND_1 ? Round.ROUND_2 : Round.FINAL_ROUND;
                router.goToLevel(match.p1.charName, match.p2.charName, round, previousLoser);
            }
            return true;
        }
        return false;
    }
}
