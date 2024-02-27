import { expect, test } from 'vitest'
import { sum} from '@/scripts/sum'

test('add 1 + 2 to equals 3', () => {
    expect(sum(1,2)).toBe(3);
})