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

export { getWeekAgo, getMonthAgo, getSixMonthAgo, getYearAgo }