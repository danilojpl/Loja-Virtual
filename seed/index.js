const axios = require('axios')
const _ = require('lodash')

const categorias = require('./data')

const url = res => `https://lojavirtual-79137-default-rtdb.firebaseio.com/${res}.json?auth=7iCXnlTCgJdNKmu2Vtjfx6wNGWe1PyLynyx4vLT4`

categorias.forEach(async function ({ produtos, ...categoria }) {
  await axios.post(url('categorias'), categoria)

  produtos.forEach(async function ({ atributos, ...produto }) {
    const atributos_arr = []
    
    Object.keys(atributos).forEach(function (chave) {
      atributos_arr.push({ chave, valor: atributos[chave] })
    })

    await axios.post(url('produtos'), {
      ...produto,
      atributos: atributos_arr,
      categoria: categoria.id
    })
  })
})
