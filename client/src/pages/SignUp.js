import axios from 'axios'
import { useEffect, useState } from 'react'

const SignUp = () => {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirm, setConfirm] = useState('')
  const [allFieldsFilledOut, setAllFieldsFilledOut] = useState(false)
  const [status, setStatus] = useState('')

  useEffect(() => {
    const databaseFields = {
      name,
      email,
      password,
      confirm,
    }

    setAllFieldsFilledOut(
      Object.keys(databaseFields).reduce((fieldsEmpty, key) => {
        if (databaseFields[key].trim().length === 0) {
          fieldsEmpty = false
        }
        return fieldsEmpty
      }, true),
    )
  }, [name, email, password, confirm])

  const checkEmail = (emailToTest) => {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailToTest)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!checkEmail(email)) {
      setStatus('Email is invalid')
      return
    }

    const hasProperLength = password.length >= 10 && password.length <= 25
    let hasCapital = false
    let hasNumber = false
    const capitalRegex = /[A-Z]/
    const numberRegex = /[0-9]/

    for (const char of password) {
      if (capitalRegex.test(char)) {
        hasCapital = true
      } else if (numberRegex.test(char)) {
        hasNumber = true
      }
    }

    if (!hasProperLength || !hasCapital || !hasNumber) {
      setStatus(
        'Password must contain a capital letter, a number, and be between 10-25 characters',
      )
      return
    }

    if (password !== confirm) {
      setStatus('Passwords must match')
      return
    }

    let signUpErr = false
    const signUpResponse = await axios
      .post('http://localhost:8080/user', {
        name,
        email,
        password,
      })
      .catch(() => {
        signUpErr = true
      })

    if (signUpErr) {
      setStatus('An error has occurred')
      return
    }

    const signUpData = signUpResponse.data

    if (!signUpData.success) {
      setStatus(signUpData.message)
      return
    }

    setStatus('Success!')
  }

  return (
    <>
      <h1>Please Sign Up</h1>
      <form onSubmit={(e) => handleSubmit(e)}>
        <input
          type="text"
          spellCheck={false}
          placeholder="Name"
          onChange={(e) => setName(e.target.value)}
        />
        <input
          type="text"
          spellCheck={false}
          placeholder="Email"
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="Confirm Password"
          onChange={(e) => setConfirm(e.target.value)}
        />
        <button type="submit" disabled={!allFieldsFilledOut}>
          Submit
        </button>
      </form>
      {status.length !== 0 && <p>{status}</p>}
    </>
  )
}

export default SignUp
