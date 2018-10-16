package dk

import dk.cutelanguage.CuteLanguageToBrainfuckConverter
import io.github.djhworld.bf.Runner
import io.github.djhworld.bf.compile.Compiler
import io.github.djhworld.bf.vm.Machine
import java.io.File
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths.get

fun main(args: Array<String>) {

    val program  = if (args.isEmpty()) {
        println("Write code to execute:")

        readLine()!!
    }
    else{
        val firstArgument = args.first()
        val fileName = when(firstArgument){
            "-c", "-convert" -> {
                convertBrainfuckToCute(args)
                return
            }
            else -> firstArgument
        }


        String(readAllBytes(get(fileName)))
    }


    val compilationResult = CuteLanguageToBrainfuckConverter()
            .compileToBrainfuck(program)

    if(compilationResult.isSuccess){
        val brainfuckCode = compilationResult.result

        runBrainfuckCode(brainfuckCode)
    }
    else{
        println(compilationResult.errors)
    }
    println()
}

private fun runBrainfuckCode(brainfuckCode: String) {
    val runner = Runner(
            Compiler(),
            Machine(System.`in`, System.out, ByteArray(30000))
    )

    runner.run(brainfuckCode)
}

private fun convertBrainfuckToCute(args: Array<String>) {
    if(args.size < 2){
        throw IllegalArgumentException("No file provided for convert")
    }

    val pathToBrainfuckCode = args[1]

    val brainfuckFile = File(pathToBrainfuckCode)
    val cuteFilename = "${brainfuckFile.parent}${File.separator}${brainfuckFile.nameWithoutExtension}.cute"

    val brainfuckCode = String(readAllBytes(get(pathToBrainfuckCode)))

    val cuteLanguageCode = CuteLanguageToBrainfuckConverter().convertToCuteLanguage(brainfuckCode)

    with(File(cuteFilename)){
        createNewFile()
        writeText(cuteLanguageCode)
    }

    println("Convert successfull")
    return
}