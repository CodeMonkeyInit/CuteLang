package dk.cutelanguage

data class CompilationResult(val isSuccess: Boolean, val result: String = "", val errors: String = "")

class CuteLanguageToBrainfuckConverter {
    private val cuteDictionary = mapOf(
            "мяу" to ">",
            "миу" to "<",
            "кусь" to "+",
            "кись" to "-",
            "лизь" to ".",
            "цем" to ",",
            "хрум" to "[",
            "ням" to "]")

    private val brainfuckDictionary = cuteDictionary.entries.associateBy ({ it.value }) {it.key}

    fun compileToBrainfuck(code: String): CompilationResult {
        val instructions = code.split(' ')

        val brainFuckInstructions = instructions.map {
            val brainfuckIntruction = cuteDictionary[it]

            if(brainfuckIntruction != null){
                brainfuckIntruction
            }
            else{
                val errorPosition = code.indexOf(it)
                return CompilationResult(false,
                        errors =  "Compile error on poistion ${errorPosition}. Unexpected symbol ${it}")
            }
        }

        return CompilationResult(true, brainFuckInstructions.joinToString(""))
    }

    fun convertToCuteLanguage(brainfuckCode: String): String{
        val cuteLanguageLexemes = brainfuckCode.mapNotNull { brainfuckDictionary[it.toString()] }

        return cuteLanguageLexemes.joinToString(" ")
    }

}
