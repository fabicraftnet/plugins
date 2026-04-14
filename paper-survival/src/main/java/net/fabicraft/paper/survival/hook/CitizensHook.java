package net.fabicraft.paper.survival.hook;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitFactory;
import net.citizensnpcs.api.trait.TraitInfo;
import net.fabicraft.paper.survival.npc.RoleplayTrait;

import java.util.List;

public final class CitizensHook implements Hook {
	@Override
	public void register() {
		TraitFactory factory = CitizensAPI.getTraitFactory();
		List.of(
				RoleplayTrait.class
		).forEach(traitClass -> factory.registerTrait(TraitInfo.create(traitClass)));
	}
}
