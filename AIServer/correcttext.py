from autocorrect import Speller

speller = {
	"en": Speller("en")
}

def correct(text, lang="en", fast=False):
	if (lang not in speller):
		speller[lang] = Speller(lang)
	
	speller[lang].fast = fast
	
	return speller[lang](text)