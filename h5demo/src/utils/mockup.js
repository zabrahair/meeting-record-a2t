import moment from 'moment'

export function mockupChart1(){
  var recordTpl = {'date': '1970-01-01', 'pv': 0, 'uv': 0, 'orders': 0}
  var today = new Date()
  for (var i = 0; i < 30; i++) {
    var obj = Object.assign({}, this.chartCompare.recordTpl)
    obj.date = moment(today.setDate(today.getDate() - 1)).format('YYYY-MM-DD')
    obj.pv = Math.random() * 1000 % 1
    obj.uv = Math.random() * 1000 % 1
    obj.orders = Math.random() * 1000 % 1
    this.chartCompare.data.rows.push(obj)
  }

  // sort by date asc
  this.chartCompare.data.rows.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
}
