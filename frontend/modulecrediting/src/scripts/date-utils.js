function getMidnight () {
    const date = new Date()
    date.setHours(0, 0, 0, 0)
    return date
}

function getWeekAgo () {
    const date = getMidnight()
    date.setDate(date.getDate() - 7)
    return date
}

function getMonthAgo () {
    const date = getMidnight()
    date.setMonth(date.getMonth() - 1)
    return date
}

function getSixMonthAgo () {
    const date = getMidnight()
    date.setMonth(date.getMonth() - 6)
    return date
}

function getYearAgo () {
    const date = getMidnight()
    date.setFullYear(date.getFullYear() - 1)
    return date
}

function getFormattedDate (date) {
    return date.getDate().toString().padStart(2, '0')
        + '.' + (date.getMonth() + 1).toString().padStart(2, '0')  // month is zero based
        + '.' + date.getFullYear().toString()
}

function parseRequestDate (dateString) {
    if (dateString) {
        return getFormattedDate(new Date(dateString))
    } else {
        return undefined
    }
}

export { getWeekAgo, getMonthAgo, getSixMonthAgo, getYearAgo, getFormattedDate, parseRequestDate }