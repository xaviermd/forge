package forge.ai.ability;

import forge.ai.*;
import forge.game.ability.AbilityFactory;
import forge.game.player.Player;
import forge.game.spellability.AbilitySub;
import forge.game.spellability.SpellAbility;

public class ImmediateTriggerAi extends SpellAbilityAi {
    // TODO: this class is largely reused from DelayedTriggerAi, consider updating

    @Override
    public boolean chkAIDrawback(SpellAbility sa, Player ai) {
        String logic = sa.getParamOrDefault("AILogic", "");
        if (logic.equals("Always")) {
            return true;
        }

        SpellAbility trigsa = null;
        if (sa.hasAdditionalAbility("Execute")) {
            trigsa = sa.getAdditionalAbility("Execute");
        } else {
            trigsa = AbilityFactory.getAbility(sa.getHostCard(), sa.getParam("Execute"));
        }
        trigsa.setActivatingPlayer(ai);

        if (trigsa instanceof AbilitySub) {
            return SpellApiToAi.Converter.get(trigsa.getApi()).chkDrawbackWithSubs(ai, (AbilitySub)trigsa);
        } else {
            return AiPlayDecision.WillPlay == ((PlayerControllerAi)ai.getController()).getAi().canPlaySa(trigsa);
        }
    }

    @Override
    protected boolean doTriggerAINoCost(Player ai, SpellAbility sa, boolean mandatory) {
        SpellAbility trigsa = null;
        if (sa.hasAdditionalAbility("Execute")) {
            trigsa = sa.getAdditionalAbility("Execute");
        } else {
            trigsa = AbilityFactory.getAbility(sa.getHostCard(), sa.getParam("Execute"));
        }
        AiController aic = ((PlayerControllerAi)ai.getController()).getAi();
        trigsa.setActivatingPlayer(ai);

        if (!sa.hasParam("OptionalDecider")) {
            return aic.doTrigger(trigsa, true);
        } else {
            return aic.doTrigger(trigsa, !sa.getParam("OptionalDecider").equals("You"));
        }
    }

    @Override
    protected boolean canPlayAI(Player ai, SpellAbility sa) {
        String logic = sa.getParamOrDefault("AILogic", "");
        if (logic.equals("Always")) {
            return true;
        }

        SpellAbility trigsa = null;
        if (sa.hasAdditionalAbility("Execute")) {
            trigsa = sa.getAdditionalAbility("Execute");
        } else {
            trigsa = AbilityFactory.getAbility(sa.getHostCard(), sa.getParam("Execute"));
        }
        trigsa.setActivatingPlayer(ai);
        return AiPlayDecision.WillPlay == ((PlayerControllerAi)ai.getController()).getAi().canPlaySa(trigsa);
    }

}