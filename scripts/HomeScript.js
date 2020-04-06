function onLoad() {
    if(checkCookie("loaded")) return;
    addCookie("loaded","true");
    document.getElementsByTagName("body")[0].classList.add("fadeIn");
}

function checkCookie(name) {
    return accessCookie(name) != "";
}

function accessCookie(name) {
    let cookieName = name+"=";
    let allCookies = document.cookie.split(";");
    for(let i=0; i < allCookies.length; i++) {
        let temp = allCookies[i].trim();
        if(temp.indexOf(cookieName) == 0)
            return temp.substring(temp.indexOf(cookieName),temp.length);
    }
    return "";
}

function getCookie(name) {
    let cookie = accessCookie(name);
    return cookie.substring(cookie.indexOf("=") + 1,cookie.length);
}

function addCookie(name, value) {
    document.cookie = name+"="+value+";";
}

function setCookie(name, value) {
    document.cookie = name+"="+value+";";
}

function avg(array) {
    let avg = 0;
    for(let i = 0; i < array.length; i++) avg += array[i];
    return avg / array.length;
}

function drawChart(text) {
    const data          = parseData(text);
    let chart_xLabels   = [];
    
    const chart_data    = [];
    const chart_colors  = [];
    const chart_labels  = [];

    if(data.dates.length == 1)
        chart_xLabels = data.time_logs[0];
    else {
        chart_xLabels = data.dates;
    }

    if(document.getElementById("checkbox-voltage").checked == true) {
        chart_data.push(data.dates.length == 1 ? data.volt_log[0] : data.avg_volt);
        chart_colors.push('rgb(255, 99, 132)');
        chart_labels.push('voltage');
    }
    if(document.getElementById("checkbox-current").checked == true) {
        chart_data.push(data.dates.length == 1 ? data.curr_log[0] : data.avg_curr);
        chart_colors.push('rgb(132, 255, 99)');
        chart_labels.push('current');
    }
    if(document.getElementById("checkbox-frequency").checked == true) {
        chart_data.push(data.dates.length == 1 ? data.freq_log[0] : data.avg_freq);
        chart_colors.push('rgb(99, 132, 255)');
        chart_labels.push('frequency');
    }
    if(document.getElementById("checkbox-power").checked == true) {
        chart_data.push(data.dates.length == 1 ? data.powr_log[0] : data.avg_powr);
        chart_colors.push('rgb(132, 99, 255)');
        chart_labels.push('power');
    }
    
    var ctx = document.getElementById('chart').getContext('2d');
    drawLineChart(ctx,chart_xLabels,chart_labels,chart_data,chart_colors);
}

let chart;
const cData = {};
const option = {
    maintainAspectRatio : false,
    responsive  : true,
    tooltips    : { mode: 'index',intersect: false   },
    hover       : { mode: 'nearest', intersect: true },
    scales      : {
                    xAxes: [ { display: true, scaleLabel: { display: true, labelString: 'time' }                     } ],
                    yAxes: [ { display: true, scaleLabel: { display: true, labelString: 'Volts / Amp / HZ / Watts' } } ]
    }
    
};

function drawLineChart(ctx, xLabels, datalabel, data, colors) {
    cData["labels"] = xLabels;
    cData["datasets"] = [];

    if(data != null) {
        for(var i =0; i < data.length; i++) {
            cData["datasets"].push({
                label : datalabel[i],
                backgroundColor: colors[i],
                borderColor: colors[i],
                data : data[i],
                fill: false,
            });
        }
    }
    if(chart == null) {
        chart = new Chart(ctx,{
            type : 'line',
            data : cData,
            options : option
        });
    } else
        chart.update();
}

function parseData(text) {
    const trDataLog = JSON.parse(text);
    const maxdays = trDataLog.max_keys;

    const dates = [];
    const time_logs = [];

    const volt_log = [];
    const curr_log = [];
    const freq_log = [];
    const powr_log = [];
    
    const avg_volt = [];
    const avg_curr = [];
    const avg_freq = [];
    const avg_powr = [];

    for(let day = 0; day < maxdays; day++) {
        let day_log = trDataLog[day];
        dates.push(day_log.d);

        const max_no_of_logs = day_log.max_keys;
        
        const day_time_logs = [];
        const day_volt_logs = [];
        const day_curr_logs = [];
        const day_freq_logs = [];
        const day_powr_logs = [];

        for(let log = 0; log < max_no_of_logs; log++) {
            day_time_logs.push(day_log[log].t);
            day_volt_logs.push(day_log[log].v);
            day_curr_logs.push(day_log[log].i);
            day_freq_logs.push(day_log[log].f);
            day_powr_logs.push(day_log[log].v * day_log[log].i);
        }

        volt_log.push(day_volt_logs);
        curr_log.push(day_curr_logs);
        freq_log.push(day_freq_logs);
        powr_log.push(day_powr_logs);

        avg_volt.push(avg(day_volt_logs));
        avg_curr.push(avg(day_curr_logs));
        avg_freq.push(avg(day_freq_logs));
        avg_powr.push(avg(day_powr_logs));

        time_logs.push(day_time_logs);
    }

    return {dates, time_logs, volt_log, curr_log, freq_log, powr_log, avg_volt, avg_curr, avg_freq, avg_powr };
}