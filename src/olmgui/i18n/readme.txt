NOTES FOR TRANSLATION
---------------------

Translating the topicmaps.properties set of files is not a problem, 
as they are plain text (no arguments). 


The messages.properties is more problematic, as there are plenty of 
placeholders (materialised by {0}, {1}, {2}, etc) that are filled at run-time 
with some particular values. 

There is no easy way of indicating how it should be translated so below 
is a list of all template with placeholders, giving information about which 
values (and which format) are used.

ATTRIBUTE.*		[all templates are plain text]

In the descriptions of the various template, I will use the following coding:
- USER  refer to the name of the user, as stored in LEAM
- BDESC refer to the belief descriptor, verbalised as a "your ability to ...." or 
		"you monitoring of your effort on ..." (see the DESCRIPTOR.* template)
- LEVEL refer to the ability levels (levelI, level II, etc.) or the judgement.
		Verbalisation are taken from the OLMTopicConfig.XXX.LevelY templates,
		where XXX is the ability (METACOG, MOTIV, AFFECT, etc) associated with the 
		query and Y is the level (1 to 4).
- ATTRIBUTE.XXX refers to the verbalisation (name) of the XXX  attribute
- ATTRIBUTE.XXX.* refers to the verbalisation of the value associated with the attribute XXX
		(see comments with this list of key/value)
- TOPIC.XXX.YYY refers to the verbalisation (title) of the topic id YYY from the map XXX
		(ie TOPIC.MOTIV.interest for interest, etc).
- TOPIC.XXX.* refers to the verbalisation (title) of one of the topic from the map XXX
		(the topic identifer is determined at run-time, depending on the exercise, on the belief descriptor, etc)
- {number} refers to a number display numerically (ie 1 NOT one)
- {percent} refers to a percentage display numerically (ie 25.45%)

[placeholders for descriptor take their value from the verbalisation of 
topic maps (cf topicmaps.properties), using the identifier of the topic]
DESCRIPTOR.AFFECT    = your {satisfaction} of
DESCRIPTOR.CAPES     = the buggy-rule {misconception_65}
DESCRIPTOR.COMPET    = your ability to {Solve problems}
DESCRIPTOR.DOMAIN    = on {Chain Rule}
DESCRIPTOR.METACOG   = your {Monitoring} of
DESCRIPTOR.MOTIV     = your {effort} in

[these are used to change the sentence from "your ability ..." to "my ability", 
for dealing with OLM vs learner utterances]
DESCRIPTOR.CASE.OLM  = your
DESCRIPTOR.CASE.USER = my

DlgErrorMsg.*	[all templates are plain text]

DlgMove.* [for the dilaogue move, only those with placeholders are described below]

DlgMove.Agree.Disagree.*                        = I disagree with your claim that I am {LEVEL}
DlgMove.Baffled.CLAIM.*                         = I don''t understand why you think I''m {LEVEL}.
DlgMove.Baffled.SUBCLAIM                      = Could you tell me more about your evidence on the {ATTRIBUTE.PERFORMANCE.*} group?

DlgMove.HereIs.BACKING                        = Here is what I know about evidence {ATTRIBUTE.INDEX }
DlgMove.HereIs.BACKING.ExerciseFinished       = Because you did this {ATTRIBUTE.DIFFICULTY.*} exercise with a {ATTRIBUTE.PERFORMANCE.*} performance.
DlgMove.HereIs.BACKING.ExerciseStep           = Because you did this {ATTRIBUTE.DIFFICULTY.*} step with a {ATTRIBUTE.PERFORMANCE.*} performance.
DlgMove.HereIs.BACKING.OLMChallenge           = Because you told me that {BDESC} was not {LEVEL} but {LEVEL}
DlgMove.HereIs.BACKING.OLMChallenge.AGREE     = Because you agreed with me that {BDESC} was {LEVEL}
DlgMove.HereIs.BACKING.OLMChallenge.DISAGREE  = Because you told me that {BDESC} was not {LEVEL} but {LEVEL}
DlgMove.HereIs.BACKING.OLMMetacog             = Because you showed a {ATTRIBUTE.MONITORING.*} tenacity in {TOPIC.METACOG.*} {BDESC}.
DlgMove.HereIs.BACKING.SelfReport             = Because you told me that your {TOPIC.AFFECT.*} was {ATTRIBUTE.SATISFACTION.*} on this exercise.
DlgMove.HereIs.BACKING.SituationFactorChanged = Because your {TOPIC.MOTIV.*} on this exercise changed to {ATTRIBUTE.EFFORT.*}

DlgMove.HereIs.CLAIM                          = Here is the evidence for me to think you are {LEVEL}
DlgMove.HereIs.CLAIM.flat                     = Evidence is ambivalent about your ability so I can only make a safe guess.
DlgMove.HereIs.CLAIM.highagainst              = Even if the most likely justification would be to say {LEVEL}, I still have to take into account the other levels.
DlgMove.HereIs.CLAIM.highfor                  = Because most of the collected evidence strongly supports you to be {LEVEL}.
DlgMove.HereIs.CLAIM.lowagainst               = The dominant trait may be {LEVEL}, but I cannot dismiss the evidence supporting the other levels.
DlgMove.HereIs.CLAIM.lowfor                   = Because there is a marginal amount of evidence supporting my claim for {LEVEL}.

DlgMove.HereIs.DATA                           = Here are all the individual pieces of evidence for me to think you are {LEVEL}
DlgMove.HereIs.DATA.NOCLUSTER                 = Here are all the individual pieces of evidence for me to think you are {LEVEL}
DlgMove.HereIs.DATA.NOCLUSTER.1               = On all the pieces of evidence I can use to justify you being {LEVEL}, these {number} are the most important.
DlgMove.HereIs.DATA.NOCLUSTER.2               = There are the {number} pieces of evidence I can use to justify you being {LEVEL}. Let''s have a look at the most important.
DlgMove.HereIs.DATA.PERFORMANCE               = Looking at the evidence according to your {ATTRIBUTE.PERFORMANCE}, my judgement is mostly based on the {number} piece(s) in the {ATTRIBUTE.PERFORMANCE.*} group.
DlgMove.HereIs.DATA.PERFORMANCE.1             = Looking at the evidence according to your {ATTRIBUTE.PERFORMANCE}, my judgement is mostly based on the {number} piece(s) in the {ATTRIBUTE.PERFORMANCE.*} group.
DlgMove.HereIs.DATA.PERFORMANCE.2             = Looking at the evidence according to your {ATTRIBUTE.PERFORMANCE}, my judgement is mostly based on the {number} piece(s) in the {ATTRIBUTE.PERFORMANCE.*} group.

DlgMove.HereIs.SUBCLAIM                       = Here are the most important pieces of evidence showing {ATTRIBUTE.PERFORMANCE.*} {ATTRIBUTE.PERFORMANCE}
DlgMove.HereIs.SUBCLAIM.1                     = Among the evidence showing {ATTRIBUTE.PERFORMANCE.*} {ATTRIBUTE.PERFORMANCE}, these {number} are the most important.
DlgMove.HereIs.SUBCLAIM.2                     = {number} among the {number} pieces of evidence are strong enough to explain {ATTRIBUTE.PERFORMANCE.*} {ATTRIBUTE.PERFORMANCE}.

DlgMove.Perhaps.Ignore                        = I''m afraid I know nothing on {BDESC}.
DlgMove.Perhaps.Ignore.1                      = I''m afraid I have no evidence on {BDESC}.
DlgMove.Perhaps.Judgment                      = I think your are {LEVEL} on {BDESC}.
DlgMove.Perhaps.Judgment.1                    = {BDESC} seems to be at {LEVEL}.
DlgMove.Perhaps.Judgment.2                    = I think your are {LEVEL} .

DlgMove.ShowMe.Think                          = Show me what you think about {BDESC}.
DlgMove.ShowMe.Think.1                        = Give me your judgement on {BDESC}.
DlgMove.Startup.LeAM                          = Welcome my dear {USER}. Why don''t you have a look at {BDESC}?
DlgMove.Startup.LeAM.1                        = {USER}, can I suggest to have a look at {BDESC}?
DlgMove.Startup.OLM                           = Welcome {USER}.
DlgMove.Startup.OLM.1                         = Nice to see you back, {USER}.
DlgMove.Startup.User                          = Welcome my dear {USER}. What brings you here today?
DlgMove.Startup.User.1                        = Ready for a journey into yourself, {USER}?

DlgMove.TellMeMore.DESCRIPTOR                 = You are telling me which ability to judge. Currently, you are looking at {BDESC}.

DlgMove.TellMeMore.SUMMARY                    = This graph represents my judgement of your {BDESC}, on a continuous scale between {LEVEL} and {LEVEL}.
DlgMove.TellMeMore.SUMMARY.ABOVE              = Being very close to the {LEVEL} indicator, my conviction on my judgement is therefore quite strong.
DlgMove.TellMeMore.SUMMARY.ABOVE.AWAY         = Since your ability level is quite distant from the {LEVEL} indicator, my conviction is not very strong, as you could as well be {LEVEL}.
DlgMove.TellMeMore.SUMMARY.BELOW              = Being very close to the {LEVEL} indicator, my conviction on my judgement is therefore quite strong.
DlgMove.TellMeMore.SUMMARY.BELOW.AWAY         = Since your ability level is quite distant from the {LEVLE} indicator, my conviction is not very strong, as you could as well be {LEVEL}.
DlgMove.Unravel.Suggest                       = Perhaps you should explore {BDESC}.
DlgMove.Unravel.Suggest.1                     = Why not having a look at {BDESC}?
DlgMove.Unravel.Suggest.2                     = I will suggest to look at {BDESC}.
DlgMove.Unravel.Urge                          = We didn''t finish our discussion on {BDESC} last time. Why don''t we have a look again?
DlgMove.Unravel.Urge.1                        = Why don''t we have a look at {BDESC} again?
DlgMove.WindUp.Accept                         = I am {ATTRIBUTE.CHLGCONFID.*} to be {LEVLE} on {BDESC}.

MASS.DECAYED.description           = This interaction suggest a {percent} chance that you are {LEVEL}
MASS.DECAYED.description.I2IV      = Ignorance related to this interaction amount to {percent}
MASS.DECAYED.description.empty     = This interaction does not suggest you are {LEVEL}
MASS.EVIDENCE.description          = This interaction initially suggested a {percent} chance that you were at {LEVEL}
MASS.MASSDISTRIB.description       = {percent} of all collected information suggest you are {LEVEL}
MASS.MASSDISTRIB.description.0     = {percent} of all collected information are conflicting evidence
MASS.MASSDISTRIB.description.I2IV  = {percent} of all collected information amount to ignorance
MASS.MASSDISTRIB.description.empty = no evidence for {LEVEL}
MASS.PIGNISTIC.description         = Evidence suggests that there is a {percent} chance that you are {LEVEL}

OLMGraphBrowser.Toulmin.Claim           = Claim : {LEVEL}

OLMGraphBrowser.Toulmin.Descriptor.Hint = The beliefs we are now exploring.<BR>Represent my judgement on your <b>{OLMTopicConfig.*}</b>.<BR>More precisely, it is about <a href=''{BDESC}''>{BDESC}</a>.

OLMGraphBrowser.Toulmin.SubClaim        = {ATTRIBUTE.PERFORMANCE} {ATTRIBUTE.PERFORMANCE.*} : {number}
OLMGraphBrowser.Toulmin.SubClaim.Other  = Others: {number}


