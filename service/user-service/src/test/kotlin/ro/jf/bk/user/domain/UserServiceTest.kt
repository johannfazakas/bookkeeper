package ro.jf.bk.user.domain

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
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

        val result = userService.list()

        assertThat(result.toList()).contains(user1, user2)
        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun `should get user by id`(): Unit = runBlocking {
        val userId = randomUUID()
        val user = User(userId, "user")
        whenever(userRepository.findById(userId)).thenReturn(user)

        val result = userService.findById(userId)

        assertThat(result).isEqualTo(user)
        verify(userRepository, times(1)).findById(userId)
    }

    @Test
    fun `should retrieve empty on get user by id when missing`(): Unit = runBlocking {
        val userId = randomUUID()
        whenever(userRepository.findById(userId)).thenReturn(null)

        val result = userService.findById(userId)

        assertThat(result).isNull()
        verify(userRepository, times(1)).findById(userId)
    }

    @Test
    fun `should get user by username`(): Unit = runBlocking {
        val user = User(randomUUID(), "user")
        whenever(userRepository.findByUsername("user")).thenReturn(user)

        val result = userService.findByUsername("user")

        assertThat(result).isEqualTo(user)
        verify(userRepository, times(1)).findByUsername("user")
    }

    @Test
    fun `should retrieve empty on get user by username when missing`(): Unit = runBlocking {
        whenever(userRepository.findByUsername("user")).thenReturn(null)

        val result = userService.findByUsername("user")

        assertThat(result).isNull()
        verify(userRepository, times(1)).findByUsername("user")
    }

    @Test
    fun `should create user`(): Unit = runBlocking {
        val username = "user"
        val user = User(randomUUID(), username)
        val command = CreateUserCommand(username)
        whenever(userRepository.save(command)).thenReturn(user)

        val result = userService.create(command)

        assertThat(result).isEqualTo(user)
        verify(userRepository, times(1)).save(command)
    }

    @Test
    fun `should throw exception on create user when it exists`(): Unit = runBlocking {
        val username = "user"
        val user = User(randomUUID(), username)
        val command = CreateUserCommand(username)
        whenever(userRepository.findByUsername(username)).thenReturn(user)

        assertThrows<UserExistsException> { userService.create(command) }
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `should delete user`(): Unit = runBlocking {
        val userId = randomUUID()
        userService.removeById(userId)

        verify(userRepository, times(1)).deleteById(userId)
    }
}
