import { useEffect, useState } from 'react'
import {
  deleteTodo,
  getName,
  getNewAccessToken,
  getTodos,
  postNewTodo,
  signOut,
  updateTodoText,
} from '../api/serverCalls'

const Home = () => {
  const [info, setInfo] = useState('')
  const [newTodo, setNewTodo] = useState('')
  const [isLoggedIn, setIsLoggedIn] = useState(
    localStorage.getItem('SPRING TODO REFRESH JWT') !== null,
  )
  const [todos, setTodos] = useState([])
  const [todoToUpdate, setTodoToUpdate] = useState(null)
  const [todoUpdateText, setTodoUpdateText] = useState('')

  const handleSignOut = async () => {
    const signOutSuccessful = await signOut()
    if (!signOutSuccessful) {
      setInfo('Error signing out')
    }

    localStorage.removeItem('SPRING TODO JWT')
    localStorage.removeItem('SPRING TODO REFRESH JWT')

    setInfo('Signed out')
    setIsLoggedIn(false)
  }

  const handleNewTodo = async (e) => {
    e.preventDefault()

    const queryResponse = await postNewTodo(newTodo)
    if (queryResponse) {
      await fetchTodos()
      setInfo('Success!')
      setNewTodo('')
    } else {
      setInfo('Error')
    }
  }

  const updateTodo = async (e) => {
    e.preventDefault()

    const queryResponse = await updateTodoText(todoUpdateText, todoToUpdate.id)
    if (queryResponse) {
      await fetchTodos()
      setInfo('Success!')
      setTodoToUpdate(null)
    } else {
      setInfo('Error')
    }
  }

  const deleteTodoTest = async () => {
    const queryResponse = await deleteTodo(21)
    if (queryResponse) {
      await fetchTodos()
      setInfo('Success!')
    } else {
      setInfo('Error')
    }
  }

  const fetchTodos = async () => {
    const queryResponse = await getTodos()
    setTodos(queryResponse)
  }

  useEffect(() => {
    fetchTodos()
  }, [])

  return (
    <>
      <h1>Home</h1>
      <button style={{ maxWidth: '50px' }} onClick={() => handleSignOut()}>
        Sign Out
      </button>
      <button style={{ maxWidth: '50px' }} onClick={() => deleteTodoTest()}>
        Delete todo test
      </button>
      <div className="spacer" />
      {isLoggedIn && (
        <>
          <form onSubmit={(e) => handleNewTodo(e)}>
            <input
              type="text"
              placeholder="New Todo"
              value={newTodo}
              onChange={(e) => setNewTodo(e.target.value)}
            />
            <button disabled={newTodo.trim().length === 0} type="submit">
              Submit
            </button>
          </form>
        </>
      )}
      {info.length !== 0 && <p>{info}</p>}
      <div className="todolist">
        {todos.length !== 0 &&
          todos.map((todo) => {
            return (
              <div key={todo.id} className="todo">
                <div>{todo.text}</div>
                <div className="link" onClick={() => setTodoToUpdate(todo)}>
                  update
                </div>
              </div>
            )
          })}
      </div>
      {todoToUpdate !== null && (
        <form onSubmit={(e) => updateTodo(e)}>
          <input
            type="text"
            defaultValue={todoToUpdate.text}
            placeholder="Update todo"
            onChange={(e) => setTodoUpdateText(e.target.value)}
          />
          <button type="submit" disabled={todoUpdateText.trim().length === 0}>
            Update
          </button>
        </form>
      )}
    </>
  )
}

export default Home
