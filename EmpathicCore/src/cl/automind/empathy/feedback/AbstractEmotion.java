package cl.automind.empathy.feedback;

import interfaces.structural.INamed;
import util.ArrayUtils;

public abstract class AbstractEmotion implements INamed{
	// <metadata-fields>
	private final String name;
	private final String[] families;
	// </metadata-fields>
	public AbstractEmotion(){
		// <metadata-fields-init>
		//TODO: Implement metadata corrections
		EmotionMetadata metadata = getClass().getAnnotation(EmotionMetadata.class);
		name = metadata != null ? metadata.name() : getClass().getSimpleName().replaceAll("Emotion", "");
		families = metadata != null ? metadata.families() : EmotionMetadata.FAMILIES;
		// </metadata-fields-init>
	}
	@Override public final String getName(){
		return name;
	}
	public boolean hasFamily(String familyName){
		if (familyName.equals(EmotionMetadata.DEFAULT)) return true;
		return ArrayUtils.contains(getFamilies(), familyName);
	}
	private String[] getFamilies() {
		return families;
	}
}
