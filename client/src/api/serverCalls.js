import axios from 'axios'

export const getNewAccessToken = async () => {
  try {
    const refreshToken = localStorage.getItem('SPRING TODO REFRESH JWT')
    if (!refreshToken) return { success: false }

    const queryResponse = await axios.post('http://localhost:8080/token', {
      token: refreshToken,
    })

    localStorage.setItem('SPRING TODO JWT', queryResponse.data.accessToken)
    return true
  } catch (e) {
    localStorage.removeItem('SPRING TODO JWT')
    localStorage.removeItem('SPRING TODO REFRESH JWT')
    return false
  }
}

const getName = async () => {
  try {
    const accessToken = localStorage.getItem('SPRING TODO JWT')

    const queryResponse = await axios.get('http://localhost:8080/user', {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })

    return queryResponse.data
  } catch (e) {
    if (e.response.data.status !== 403) {
      return { success: false, message: 'An error has occurred' }
    }

    try {
      await getNewAccessToken()
      return getName()
    } catch (getNewTokenErr) {
      return { success: false, message: 'An error has occurred' }
    }
  }
}

const signOut = async () => {
  try {
    const refreshToken = localStorage.getItem('SPRING TODO REFRESH JWT')
    const queryResponse = await axios.delete('http://localhost:8080/token', {
      headers: {
        Token: refreshToken,
      },
    })

    if (queryResponse.data.success) {
      return true
    }

    return false
  } catch (e) {
    return false
  }
}

const postNewTodo = async (text) => {
  try {
    const queryResponse = await axios.post(
      'http://localhost:8080/todo',
      { text },
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('SPRING TODO JWT')}`,
        },
      },
    )

    if (queryResponse.data.success) {
      return true
    }

    return false
  } catch (e) {
    if (e.response.data.status === 403) {
      try {
        await getNewAccessToken()
        return postNewTodo(text)
      } catch (getNewTokenErr) {
        return false
      }
    }

    return false
  }
}

const getTodos = async () => {
  try {
    const queryResponse = await axios.get('http://localhost:8080/todo', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('SPRING TODO JWT')}`,
      },
    })

    return queryResponse.data.todos
  } catch (e) {
    if (e.response.data.status === 403) {
      try {
        await getNewAccessToken()
        return getTodos()
      } catch {
        return []
      }
    }
    return []
  }
}

const updateTodoText = async (text, todoId) => {
  try {
    const queryResponse = await axios.put(
      'http://localhost:8080/todo',
      {
        text,
        todoId,
      },
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('SPRING TODO JWT')}`,
        },
      },
    )

    if (queryResponse.data.success) {
      return true
    }

    return false
  } catch (e) {
    if (e.response.data.status === 403) {
      try {
        await getNewAccessToken()
        return updateTodoText(text, todoId)
      } catch (getNewTokenErr) {
        return false
      }
    }

    return false
  }
}

const deleteTodo = async (todoId) => {
  try {
    const queryResponse = await axios.delete('http://localhost:8080/todo', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('SPRING TODO JWT')}`,
        todoid: todoId,
      },
    })

    if (queryResponse.data.success) {
      return true
    }

    return false
  } catch (e) {
    return false
  }
}

export { getName, signOut, postNewTodo, getTodos, updateTodoText, deleteTodo }
