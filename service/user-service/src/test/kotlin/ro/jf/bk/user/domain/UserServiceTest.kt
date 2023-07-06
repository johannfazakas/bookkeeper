package ro.jf.bk.user.domain

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ro.jf.bk.user.domain.model.CreateUserCommand
import ro.jf.bk.user.domain.model.User
import ro.jf.bk.user.domain.service.UserRepository
import ro.jf.bk.user.domain.service.UserService
import java.util.UUID.randomUUID

class UserServiceTest {
    private val userRepository = mock<UserRepository>()
    private val userService = UserService(userRepository)

    @Test
    fun `should get users`(): Unit = runBlocking {
        val user1 = User(randomUUID(), "user1")
        val user2 = User(randomUUID(), "user2")
        whenever(userRepository.findAll()).thenReturn(flowOf(user1, user2))

        val result = userService.getUsers()

        assertThat(result.toList()).contains(user1, user2)
        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun `should get user`(): Unit = runBlocking {
        val user = User(randomUUID(), "user")
        whenever(userRepository.findByUsername("user")).thenReturn(user)

        val result = userService.getUser("user")

        assertThat(result).isEqualTo(user)
        verify(userRepository, times(1)).findByUsername("user")
    }

    @Test
    fun `should retrieve empty on get user when missing`(): Unit = runBlocking {
        whenever(userRepository.findByUsername("user")).thenReturn(null)

        val result = userService.getUser("user")

        assertThat(result).isNull()
        verify(userRepository, times(1)).findByUsername("user")
    }

    @Test
    fun `should create user`(): Unit = runBlocking {
        val username = "user"
        val user = User(randomUUID(), username)
        val command = CreateUserCommand(username)
        whenever(userRepository.save(command)).thenReturn(user)

        val result = userService.createUser(command)

        assertThat(result).isEqualTo(user)
        verify(userRepository, times(1)).save(command)
    }

    @Test
    fun `should throw exception on create user when it exists`(): Unit = runBlocking {
        val username = "user"
        val user = User(randomUUID(), username)
        val command = CreateUserCommand(username)
        whenever(userRepository.findByUsername(username)).thenReturn(user)

        assertThrows<UserExistsException> { userService.createUser(command) }
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `should delete user`(): Unit = runBlocking {
        userService.deleteUser("user")

        verify(userRepository, times(1)).deleteByUsername("user")
    }
}
