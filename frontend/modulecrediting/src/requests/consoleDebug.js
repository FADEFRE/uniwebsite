export function consoleDebug(color, msgToColor, msgNoColor) {
  if (true) { //TODO set to false to remove debug logs
    if (msgNoColor === undefined) {
      console.debug("%c" + msgToColor, color)
    }
    else console.debug("%c" + msgToColor, color, msgNoColor)
  }
}