package org.thingml.xtext.validation.checks

import java.util.Set
import org.eclipse.xtext.validation.Check
import org.thingml.xtext.thingML.Configuration
import org.thingml.xtext.thingML.Property
import org.thingml.xtext.thingML.Thing
import org.thingml.xtext.thingML.ThingMLPackage
import org.thingml.xtext.validation.ThingMLValidatorCheck
import org.thingml.xtext.thingML.Enumeration
import org.thingml.xtext.constraints.ThingMLHelpers
import org.thingml.xtext.thingML.ThingMLModel
import org.thingml.xtext.validation.TypeChecker
import org.thingml.xtext.helpers.TyperHelper

class PropertyInitialization extends ThingMLValidatorCheck {
	
	def Set<Property> getUninitializedProperties(Thing t) {
		val props = newHashSet()
		// Properties from current thing
		t.properties.forEach[prop|
			if (prop.init === null)
				props.add(prop);
		]
		
		// Properties from included things
		t.includes.forEach[inc | props.addAll(getUninitializedProperties(inc))]
		
		// Remove properties initialised by set statements
		t.assign.forEach[propAssign|
			props.removeIf(prop | prop === propAssign.property)
		]
		
		return props
	}
	
	@Check(NORMAL)
	def checkPropertyInitialization(Configuration cfg) {
		cfg.instances.forEach[inst, i|
			val props = getUninitializedProperties(inst.type)
			
			// Remove properties initialised by set statements
			cfg.propassigns.forEach[propAssign|
				props.removeIf(prop | prop === propAssign.property)
			]
			
			if (!props.empty) {
				val msg = props.join("Properties (",", ",") are not initialized")[it.name]
				warning(msg, cfg, ThingMLPackage.eINSTANCE.configuration_Instances, i, "properties-not-initialized")
			}
		]
	}
	
	@Check(NORMAL)
	def checkEnumInitialization(Enumeration e) {
		if (e.typeRef !== null) {//all literal must be initialized
			if (e.literals.exists[l | l.init === null]) {
				val msg = "Enumeration " + e.name + " is typed. All literals must be initialized."
				error(msg, ThingMLHelpers.findContainingModel(e), ThingMLPackage.eINSTANCE.thingMLModel_Types, (ThingMLHelpers.findContainingModel(e) as ThingMLModel).types.indexOf(e))
			}
			e.literals.forEach[l, i |
				if (l.init !== null) {
					val litType = TypeChecker.computeTypeOf(l.init)
					if(!TyperHelper.isA(litType, e.typeRef.type)) {
						val msg = "Literal " + l.name + " must be of type " + TyperHelper.getBroadType(e.typeRef.type).name + ". Found " + TyperHelper.getBroadType(litType).name
						error(msg, e, ThingMLPackage.eINSTANCE.enumeration_Literals, i)				
					}				
				}
			]
		}
	}	
}