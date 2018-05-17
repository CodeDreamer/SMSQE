* Find the next Job using a Thing       v0.00   Feb 1988  J.R.Oakley  QJUMP
* (It's just one Job after another)
        section thing
*
        include 'dev8_mac_assert'
        include 'dev8_keys_err'
        include 'dev8_keys_sys'
        include 'dev8_keys_thg'
*
        xref    th_find
        xref    th_nxtu
        xref    th_exit
*
        xdef    th_nthu
*+++
* Find a thing, given its name, and return the Job ID of the Job that
* owns the given usage block, and the address of the next usage block:
* this allows application code to generate a list of Jobs that are
* using a Thing.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0, ITNF, IJOB
*       D1                                      Job ID of owner of block
*       D2/D3                                   smashed
*       A0      name of thing (>=3 chars)       preserved
*       A1      usage block or 0                next usage block, or first
*       A2/A3                                   smashed
*       A6      pointer to system variables     preserved
*---
th_nthu
        move.l  a1,a2                   ; keep pointer to current usage block
        jsr     th_find(pc)             ; find the Thing
        bne.s   thu_exit                ; ...oops
        jsr     th_nxtu(pc)             ; usage code can find user and next
thu_exit
        jmp     th_exit(pc)
*
        end




