const faker = require('faker')

const precos = [9.99, 19.99, 24.99, 35.00, 59.99, 99.99, 100.00, 129.99, 150.00, 199.99, 259.00, 350.00, 500.00, 799.99, 1000.00]

const produtoFake = (nome, atributos, imagens) => ({
  atributos,
  nome,
  imagens,
  id: faker.datatype.uuid(),
  descricao: faker.lorem.paragraph(),
  estoque: Math.floor(Math.random() * 20),
  marca: faker.lorem.words(2),
  preco_promocional: faker.datatype.boolean() ? faker.random.arrayElement(precos.filter((_, i) => i <= 3)) : 0,
  preco: faker.random.arrayElement(precos),
})

module.exports = ([
  {
    nome: 'acessórios',
    id: faker.datatype.uuid(),
    imagem: '',
    produtos: [
      produtoFake('Estojo Harry Potter', { cor: 'preto' }, ['https://images.tcdn.com.br/img/img_prod/723087/necessaire_viagem_hogwarts_ean_7908011747019_7245_4_20201214140607.jpg', 'https://images.tcdn.com.br/img/img_prod/723087/necessaire_viagem_hogwarts_ean_7908011747019_7245_2_20201026120233.jpg']),
      produtoFake('Mousepad Assassins Creed', { largura: '30cm', altura: '15cm' }, ['https://http2.mlstatic.com/D_NQ_NP_796872-MLB45755041039_042021-O.webp', 'https://http2.mlstatic.com/D_NQ_NP_634620-MLB45754935927_042021-O.webp']),
      produtoFake('Bottom Game of Thrones', {}, ['https://photos.enjoei.com.br/button-tyrion-e-game-of-thrones-23298340/1200xN/czM6Ly9waG90b3MuZW5qb2VpLmNvbS5ici9wcm9kdWN0cy8yNzIzNjMvMzcyODdjMmRhZGQ4Yjk1MGI2ODI5YzE0MTM3MmIxYWMuanBn']),
      produtoFake('Capa para Notebbok Baby Yoad', { cor: 'preto' }, ['https://http2.mlstatic.com/D_NQ_NP_653923-MLB44754085985_012021-O.webp'])
    ]
  },

  {
    nome: 'action figures',
    id: faker.datatype.uuid(),
    imagem: '',
    produtos: [
      produtoFake('Thanos', {}, ['https://www.fantoy.com.br/media/catalog/product/cache/3db55c74243ab666fe33f3bb10752784/t/h/thanos-marvel-gallery-diamond-min.jpg']),
      produtoFake('Doutor Estranho', {}, ['https://www.fantoy.com.br/media/catalog/product/cache/3db55c74243ab666fe33f3bb10752784/d/r/drstrange-min.jpg']),
      produtoFake('Coringa', {}, ['https://www.fantoy.com.br/media/catalog/product/cache/3db55c74243ab666fe33f3bb10752784/j/o/jokermini5.jpg']),
      produtoFake('Stan Lee', {}, ['https://www.fantoy.com.br/media/catalog/product/cache/3db55c74243ab666fe33f3bb10752784/d/r/drstrange-min.jpg']),
      produtoFake('Iron Man', {}, ['https://www.fantoy.com.br/media/catalog/product/cache/5070b15b05522f191912dd31c57262ab/e/s/estatua-iron-man-mark-lxxxv-avengers-endgame-iron-studios.jpg', 'https://www.fantoy.com.br/media/catalog/product/cache/5070b15b05522f191912dd31c57262ab/m/a/manendgame5.jpg', 'https://www.fantoy.com.br/media/catalog/product/cache/5070b15b05522f191912dd31c57262ab/m/a/manendgame5.jpg']),
      produtoFake('Capitã Marvel', {}, ['https://www.fantoy.com.br/media/catalog/product/cache/5070b15b05522f191912dd31c57262ab/c/a/capmarvel4_1.jpg', 'https://www.fantoy.com.br/media/catalog/product/cache/5070b15b05522f191912dd31c57262ab/c/a/capmarvel5.jpg'])
    ]
  },
  {
    nome: 'decoração',
    id: faker.datatype.uuid(),
    imagem: '',
    produtos: [
      produtoFake('Luminária Nuvem', { largura: '20cm', altura: '15cm', comprimento: '10cm' }, ['https://cdn.awsli.com.br/1000x1000/1225/1225697/produto/102660733/3f0fdda4d6.jpg']),
      produtoFake('Luminária Batman', { largura: '10cm', altura: '10cm', comprimento: '10cm' }, ['https://static3.tcdn.com.br/img/img_prod/460977/luminaria_morcego_71124_1_20201211171850.jpg']),
      produtoFake('Vaso de Plantas Groot', {}, ['https://static3.tcdn.com.br/img/img_prod/460977/vaso_de_planta_baby_groot_guardioes_da_galaxia_vol_2_guardians_of_the_galaxy_vol_2_cachepot_35455_1_20200616161048.jpg'])
    ]
  },
  {
    nome: 'jogos',
    id: faker.datatype.uuid(),
    imagem: '',
    produtos: [
      produtoFake('GTA V', { plataforma: 'Xbox One' }, ['https://www.ecompletocdn.com.br/i/fp/2280/1079347_151_1565637310.jpg']),
      produtoFake('Cyberpunk 2077', { plataforma: 'PS4' }, ['https://www.ecompletocdn.com.br/i/fp/2280/1110575_151_1607542770.jpg']),
      produtoFake('Doom Eternal', { plataforma: 'PS4' }, ['https://www.ecompletocdn.com.br/i/fp/2280/1168014_151_1604688254.jpg']),
      produtoFake('The Last of Us II', { plataforma: 'PS4' }, ['https://www.ecompletocdn.com.br/i/fp/2280/1096839_151_1606477427.jpg']),
      produtoFake('God of War', { plataforma: 'PS4' }, ['https://www.ecompletocdn.com.br/i/fp/2280/1104063_151_1574515659.jpg']),
      produtoFake('Dark Souls Remastered', { plataforma: 'PS4' }, ['https://www.ecompletocdn.com.br/i/fp/2280/1095714_151_1570628000.jpg']),
      produtoFake('Crash Bandicoot N\'sane Trilogy', { plataforma: 'PS4' }, ['https://www.ecompletocdn.com.br/i/fp/2280/1104391_151_1585847772.jpg']),
      produtoFake('Luigi\'s Mansion', { plataforma: 'Nintendo Switch' }, ['https://www.ecompletocdn.com.br/i/fp/2280/1087470_151_1568297308.jpg']),
      produtoFake('Injustice 2', { plataforma: 'PS4' }, ['https://www.ecompletocdn.com.br/i/fp/2280/1134383_151_1591886040.jpg'])
    ]
  },
  {
    nome: 'livros',
    id: faker.datatype.uuid(),
    imagem: '',
    produtos: [
      produtoFake('Donnie Darko', {}, ['https://images-na.ssl-images-amazon.com/images/I/51fCQh2Kw4L._AC_SX184_.jpg']),
      produtoFake('De Volta para o Futuro', {}, ['https://images-na.ssl-images-amazon.com/images/I/51E64I9fq+L.jpg', 'https://images-na.ssl-images-amazon.com/images/I/719-3E6STvL.jpg']),
      produtoFake('Naruto Gold Vol. 3', {}, ['https://images-na.ssl-images-amazon.com/images/I/91H3yNMsx-L.jpg']),
      produtoFake('2001 uma odisseia no espaço', {}, ['https://images-na.ssl-images-amazon.com/images/I/61zLkd00iuL.jpg']),
      produtoFake('Eu, roboô', {}, ['https://images-na.ssl-images-amazon.com/images/I/41Pj1UfmHWL.jpg'])
    ]
  },
  {
    nome: 'tecnologia',
    id: faker.datatype.uuid(),
    imagem: '',
    produtos: [
      produtoFake('Ryzen 5 1600', {}, ['https://images5.kabum.com.br/produtos/fotos/107545/processador-amd-ryzen-5-1600-cache-19mb-3-2ghz-3-6ghz-max-turbo-am4-yd1600bbafbox_1573653284_m.jpg']),
      produtoFake('Acer Aspire 3', { processador: 'Intel Core i7-1065G7', 'sistema operacional': 'Windows 10', memoria: '8GB DDR4' }, ['https://images6.kabum.com.br/produtos/fotos/134516/notebook-acer-aspire-3-intel-core-i3-1005g1-4gb-256gb-ssd-15-6-windows-10-home-a315-56-330j_1608729309_m.jpg']),
      produtoFake('Nintendo Switch', {}, ['https://images6.kabum.com.br/produtos/fotos/135586/nintendo-switch-32gb-1x-joycon-neon-azul-vermelho-hbdskaba2_1610110214_m.jpg']),
      produtoFake('Microfone Gamer', {}, ['https://images8.kabum.com.br/produtos/fotos/101288/microfone-gamer-hyperx-quadcast-antivibracao-led-preto-e-vermelho-hx-micqc-bk_1615553162_m.jpg']),
    ]
  }
])