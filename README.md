# Icons and Damage Modifiers

_**aka Alison, why didn't you just use Card Modifiers?**_


This mod adds 33 icons for use in card descriptions, tooltips, powers, etc. and adds the framework to add custom icons yourself.

In addition to this, this also comes packaged with Damage Modifiers (Beta), which allow you to add custom effects to damage types _without_ using any DamageInfo.DamageType enums to ensure maxium compatability. Damage Modifiers allow you to change the logic of your damage actions at each step of execution (atDamageGive -> atDamageFinalGive -> onDamageModifiedByBlock -> ignoresBlock -> onAttackToChangeDamage -> onAttack), and also allows you to define CustomTooltips and CardDescriptors (for cards at least).

Damage Modifiers are _not_ limited to cards, and will work for Relics, Monsters, Powers, etc.

Features
---

_**Icons:**_ 
Each of the 33 icons comes with a name, and the text render looks for words of the form [NameHereIcon], and compares this to the array of registered icons. Should it find a matching icon, this word is replaced by a scaled down version of the image you provide for the icon. There is no need to manually downscale your images, as defining the image size will automatically scale your icon accordingly. As a direct result, icons should be square for best results. The base 33 icons are all 32x32 pixels.

_**Damage Modifiers:**_
DamageModifierHelper comes packaged with _many_ functions that allow you to bind custom Damage Modifiers to damage actions. In order to use a Damage Modifier there are 2 steps to follow (1 step if the object using the Damage Modifier is a card):

1 - Add the Damage Modifier to your object: 
```Java
    DamageModifierManager.addModifier(YourCardRelicWhateverHere, YourDamageModifierHere)
```

2 - Bind your object to your DamageInfo (or the action): 
```Java
    this.addToBottom(new DamageAction(target, DamageModifierHelper.bindDamageInfo(this, source, damage, damageType) 
    
    //OR

    AbstractGameAction action = fancyGameAction();
    DamageModifierHelper.bindAction(this, action);
    this.addToBottom(action);
```

Check out DamageModifierHelper for a full list of binding methods.

Hooks
---

The hooks provided by damage modifiers are as follows:

```Java
boolean ignoresBlock()

boolean ignoresThorns()

boolean removeWhenActivated()

float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card)

float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card)

void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)

int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target)

void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature target)

String getCardDescriptor

TooltipInfo getCustomTooltip()

int priority

void addToTop(AbstractGameAction action)

void addToBot(AbstractGameAction action)
```
