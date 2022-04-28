import axios from 'axios'
import { useEffect, useState } from 'react'

const SignIn = () => {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [allFieldsFilledOut, setAllFieldsFilledOut] = useState(false)
  const [status, setStatus] = useState('')

  useEffect(() => {
    if (email.length === 0 || password.length === 0) {
      setAllFieldsFilledOut(false)
    } else {
      setAllFieldsFilledOut(true)
    }
  }, [email, password])

  const handleSubmit = async (e) => {
    e.preventDefault()

    let signInErr = false
    const signInResponse = await axios
      .post('http://localhost:8080/sign-in', {
        email,
        password,
      })
      .catch(() => {
        signInErr = true
      })

    if (signInErr) {
      setStatus('An error has occurred')
      return
    }

    const signInData = signInResponse.data
    if (!signInData.success) {
      setStatus(signInData.message)
      return
    }

    console.log(signInData.refreshToken)
    localStorage.setItem('SPRING TODO JWT', signInData.jwt)
    localStorage.setItem('SPRING TODO REFRESH JWT', signInData.refreshToken)
    setStatus(`Jwt is ${signInData.jwt}`)
  }

  return (
    <>
      <h1>Sign In</h1>
      <form onSubmit={(e) => handleSubmit(e)}>
        <input
          type="text"
          placeholder="Email"
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="text"
          placeholder="Password"
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit" disabled={!allFieldsFilledOut}>
          Submit
        </button>
      </form>
      {status.length !== 0 && <p>{status}</p>}
    </>
  )
}

export default SignIn
