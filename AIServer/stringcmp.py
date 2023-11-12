import difflib
import string



def custom_word_tokenize(text):
	# Split the input text into words and punctuation
	tokens = []
	current_word = ""
	for char in text:
		if char in string.whitespace:
			if current_word:
				tokens.append(current_word)
				current_word = ""
			tokens.append(char)
		elif char in string.punctuation:
			if current_word:
				tokens.append(current_word)
				current_word = ""
			tokens.append(char)
		else:
			current_word += char
	if current_word:
		tokens.append(current_word)
	return tokens

def remove_punctuation(input_string):
	# Create a translation table to remove punctuation
	translator = str.maketrans('', '', string.punctuation)
	return input_string.translate(translator)


def is_punctuation(token):
	return all(char in string.punctuation for char in token)

def is_whitespace(token):
	return all(char in string.whitespace for char in token)

def highlight_differences(str1, str2, ignore_punctuation=True):
	# Create a differ object
	differ = difflib.Differ()

	# Tokenize the input strings into words and punctuation
	tokens1 = custom_word_tokenize(str1)
	tokens2 = custom_word_tokenize(str2)

	# Get the differences between the two tokenized strings
	diff = list(differ.compare(tokens1, tokens2))

	# Initialize empty strings to store the highlighted differences
	ret = ""

	ret_tokens = []
	missing = []
	for item in diff:
		indicator, token = item[:2], item[2:]
		# print(len(ret_tokens), "=", token)
		if indicator == '  ':  # No difference
			ret += token
			ret_tokens += [token]
		elif indicator == '- ':
			if is_punctuation(token) or is_whitespace(token): ret += token
			else:
				ret += f'({token})'
				missing += [len(ret_tokens)]
				# print('miss')
			ret_tokens += [token]


	return ret_tokens, missing

if __name__ == '__main__': 
	s1 = "Winter holidays are my favorite because I get to build snowmen and have snowball fights with friends."
	s2 = "When the holidays are my favorite because I get to build snowmen and have snowball fights with my friends"
	
	print(highlight_differences(s1, s2))