* Find the next Thing                   v0.00   Feb 1988  J.R.Oakley  QJUMP
* (It's just one thing after another)
        section thing
*
        include 'dev8_mac_assert'
        include 'dev8_keys_err'
        include 'dev8_keys_sys'
        include 'dev8_keys_thg'
*
        xref    th_find
        xref    th_exit
*
        xdef    th_nthg
*+++
* Find a thing, given its name, but return the pointer to the next thing's
* linkage block.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0, ITNF, IJOB
*       D2/D3                                   smashed
*       A0      name of thing (>=3 chars)       preserved
*       A1                                      pointer to Thing
*       A2/A3                                   smashed
*       A6      pointer to system variables     preserved
*---
th_nthg
        move.l  a0,d0                   ; do we have a name?
        bne.s   fnext                   ; yes
        move.l  sys_thgl(a6),a1         ; no, point to first thing
        bra.s   thu_exit                ; and leave
fnext
        jsr     th_find(pc)             ; find the Thing
        bne.s   thu_exit                ; ...oops
        move.l  th_nxtth(a1),a1         ; point to next thing
        moveq   #0,d0
thu_exit
        jmp     th_exit(pc)
*
        end



