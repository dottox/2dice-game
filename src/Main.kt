import kotlin.math.abs
import kotlin.random.Random

fun checkInput(nums: Set<Int>, dice1: Int, dice2: Int, userResponse: List<Int>): Pair<Boolean, String> {

    // Check if size is 1 or 2
    if (userResponse.size < 1) {
        return Pair(false, "Enter a number or a combination with -\n")
    }

    // If only 1 num then check if is equal to the sum of dices
    if (userResponse.size == 1) {
        if (userResponse[0] != (dice1+dice2)) {
            return Pair(false, "That number isn't equal to the sum of dices\n")
        }
    }
    // If more than one num:
    else {

        for (num in userResponse) if (num !in 1..12) {
            return Pair(false, "Numbers not in the range of 1..12\n")
        }

        if (userResponse.size != userResponse.toSet().size) {
            return Pair(false, "Numbers cannot be equal\n")
        }

        if (!check_sum_bruteforce(userResponse, dice1 + dice2)) {
            return Pair(false, "Numbers not equal to the sum of dices\n")
        }
    }

    // Check if input nums are available
    for (num in userResponse) if (num !in nums ) {
        return Pair(false, "Numbers not availables\n")
    }

    return Pair(true, "")
}

fun check_sum_bruteforce(nums: List<Int>, target:Int): Boolean {
    var sums: List<Int> = listOf()

    for (num in nums) {
        for (sum in sums) {
            sums = sums + (sum+num)
        }

        sums = sums + num
    }

    return (target in sums)
}

fun checkLoose(nums: Set<Int>, dice1: Int, dice2: Int): Boolean {
    val sum = dice1 + dice2
    if ((dice1 in nums && dice2 in nums) && dice1 == dice2) return false
    if (sum in nums) return false

    return !check_sum_bruteforce(nums.toList(), dice1 + dice2)
}

fun main() {
    var nums: MutableSet<Int> = mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    var game = true
    var score = 0

    while (game) {
        // Dices
        var dice1: Int = abs(Random.nextInt() % 6) + 1
        var dice2: Int = abs(Random.nextInt() % 6) + 1

        // Interface
        print("Lista: ")
        for (num in nums) print("$num ")
        println("\nDado 1: $dice1")
        println("Dado 2: $dice2")

        // Checking loose
        if (dice1+dice2 !in nums) {
            if (checkLoose(nums, dice1, dice2)) {
                println("Has perdido")
                break
            }
        }

        // Aux variables
        var notPicked = true

        // Ask numbers until 1 or 2 valid
        while (notPicked) {

            // Selecting numbers
            print("Choose your numbers: ")
            var userResponse = readln()
                .split("-")
                .map(String::toInt)

            var (flag, errorMsg) = checkInput(nums, dice1, dice2, userResponse)

            // Restart the selection
            if (!flag) {
                print(errorMsg)
                continue
            }
            else {
                for (num in userResponse) {
                    score += num*10
                    nums.remove(num)
                }
            }

            notPicked = false
        }

        if (nums.size == 0) game = false
    }

    println("You loose...")
    println("Score: $score")
}