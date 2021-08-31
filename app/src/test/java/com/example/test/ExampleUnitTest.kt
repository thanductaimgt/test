package com.example.test

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testUri2() {
        testUri()
    }

    @Test
    fun regex() {
        listOf(
            "0123456789",
            "thanductaimgt@gmail.com",
            "dangPham_412_08@yopmail.com",
            "anh1998maa@gmail.com",
            "1613027@hcmut.edu.vn",
            "tai.than@tiki.vn",
            "thanh.bui2@tiki.vn",
            "binh.tran3@tiki.vn",
            "x@yahoo.com",
            "y@gmail.com",
            "phone"
        ).map {
            it.censored(4, it.length - 4)
                .also {
                    println(it)
                }
        }
    }

    @Test
    fun display_name() {
        listOf(
            ChatUserInfo(
                fullName = "    ",
                phone = "0123456789",
                email = null,
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = "0123456789",
                email = "taithan",
                nickName = "      ."
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = "0123456789",
                email = "anh2003maa@gmia,com",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = null,
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "tai.than@yki.ck",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "n@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nh@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nha@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nhan@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nhann@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nhanng@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nhangu@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nhannguy@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nhannguye@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nhannguyen@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = null,
                email = "nhannguyen1@tiki.vn",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = "0123456789",
                email = null,
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = "0123",
                email = null,
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = "0123",
                email = "null",
                nickName = "      "
            ),
            ChatUserInfo(
                fullName = "    ",
                phone = "0123",
                email = "emailemailemailemailemailemail",
                nickName = "      "
            )
        ).forEach {
            println(
                String.format(
                    "user: %s,\ndisplayName: %s\n",
                    it,
                    it.displayNameInferred
                )
            )
        }
    }
}

data class ChatUserInfo(
    private val fullName: String?,
    val email: String?,
    val phone: String?,
    val nickName: String?
) {
    fun fullName(): String? = fullName

    val displayNameInferred: String
        get() {
            return fullName()?.takeIf { it.isNotBlank() } ?: nickName?.takeIf { it.isNotBlank() }
            ?: email?.censoredEmail() ?: phone?.censoredPhone()
                .orEmpty()
        }
}

fun String.censoredEmail(): String {
    val endIndex = indexOfFirst { it == '@' }.takeIf { it != -1 } ?: length
    val startIndex = (endIndex / 2).coerceAtMost(5)
    return censored(startIndex, endIndex)
}

fun String.censoredPhone(): String {
    val endIndex = length - 3
    val startIndex = 3
    return takeIf { startIndex <= endIndex }?.censored(startIndex, endIndex)
        ?: this
}

/**
 * @param endIndex is excluded, to be analogous with replaceRange
 */
fun String.censored(startIndex: Int, endIndex: Int, censorUnit: String = "*"): String {
    return (takeIf { startIndex <= endIndex }
        ?.replaceRange(startIndex, endIndex, censorUnit.repeat(endIndex - startIndex)))
        ?: this
}
