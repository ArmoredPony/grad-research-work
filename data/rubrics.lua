---@alias Rubric
---| 'Экономика'
---| 'Спорт'
---| 'Культура'
---| 'Наука и техника'
---| 'Интернет и СМИ'
---| 'Политика'
---| 'Общество'

---@alias Tag string
---| 'economics'
---| 'sport'
---| 'culture'
---| 'science'
---| 'media'
---| 'politics'
---| 'society'

return {
  ---@type table<Tag, Rubric>
  names = {
    ['economics'] = 'Экономика',
    ['sport'] = 'Спорт',
    ['culture'] = 'Культура',
    ['science'] = 'Наука и техника',
    ['media'] = 'Интернет и СМИ',
    ['politics'] = 'Политика',
    ['society'] = 'Общество',
  },
  ---@type table<Rubric, Tag>
  tags = {
    ['Экономика'] = 'economics',
    ['Спорт'] = 'sport',
    ['Культура'] = 'culture',
    ['Наука и техника'] = 'science',
    ['Интернет и СМИ'] = 'media',
    ['Политика'] = 'politics',
    ['Общество'] = 'society'
  }
}
