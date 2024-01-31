function filterBySearch (searchString, applications) {
    return applications.filter(a => {
        const lowerSearchString = searchString.toLowerCase()
        return a['id'].includes(lowerSearchString)
            || a['modulesConnections'].some(c => c['externalModules'].some(m => m['name'].toLowerCase().includes(lowerSearchString)))
            || a['modulesConnections'].some(c => c['externalModules'].some(m => m['university'].toLowerCase().includes(lowerSearchString)))
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

function sortByDate (dateKey, applications) {
    applications.sort((a, b) => new Date(b[dateKey]).getTime() - new Date(a[dateKey]).getTime())
}

function filterApplications (filter, applications) {
    let filteredApplications = applications

    // search filter
    if (filter.searchString) {
        filteredApplications = filterBySearch(filter.searchString, filteredApplications)
    }
    // date filter
    if (filter.earliestDate) {
        filteredApplications = filterByDate(filter.earliestDate, filter.dateType, filteredApplications)
    }
    // status filter
    filteredApplications = filterByStatus(filter.statusTypes, filteredApplications)
    // course filter
    if (filter.course) {
        filteredApplications = filterByCourse(filter.course, filteredApplications)
    }

    // sorting
    sortByDate(filter.dateType, filteredApplications)

    return filteredApplications
}

export { filterApplications }