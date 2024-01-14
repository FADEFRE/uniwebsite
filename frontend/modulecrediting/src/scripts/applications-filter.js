function filterBySearch (searchString, applications) {
    return applications.filter(a => {
        const lowerSearchString = searchString.toLowerCase()
        return a['id'].includes(lowerSearchString)
    })
}

function filterByDate (earliestDate, dateKey, applications) {
    const checkDate = new Date(earliestDate)
    return applications.filter(a => new Date(a[dateKey]) > checkDate)
}

function filterByStatus (statusList, applications) {
    return applications.filter(a => statusList.includes(a['fullStatus']))
}

function filterByCourse (course, applications) {
    return applications.filter(a => a['courseLeipzig']['name'] === course)
}

function filterApplications (filter, applications) {
    let filteredApplications = applications

    // search filter
    if (filter.searchString) {
        filteredApplications = filterBySearch(filter.searchString, filteredApplications)
    }
    // date filter
    if (filter.earliestDate) {
        if (filter.dateType === 'creation') {
            filteredApplications = filterByDate(filter.earliestDate, 'creationDate', filteredApplications)
        } else if (filter.dateType === 'lastEdit') {
            filteredApplications = filterByDate(filter.earliestDate, 'lastEditedDate', filteredApplications)
        } else if (filter.dateType === 'decision') {
            filteredApplications = filterByDate(filter.earliestDate, 'decisionDate', filteredApplications)
        }
    }
    // status filter
    filteredApplications = filterByStatus(filter.statusTypes, filteredApplications)
    // course filter
    if (filter.course) {
        filteredApplications = filterByCourse(filter.course, filteredApplications)
    }

    return filteredApplications
}

export { filterApplications }