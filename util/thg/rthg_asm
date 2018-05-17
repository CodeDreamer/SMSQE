* Remove Thing from list                v0.00   Feb 1988  J.R.Oakley  QJUMP
*
        section thing
*
        include 'dev8_mac_assert'
        include 'dev8_keys_chp'
        include 'dev8_keys_err'
        include 'dev8_keys_qlv'
        include 'dev8_keys_sys'
        include 'dev8_keys_thg'
*
        xref    th_find
        xref    th_rechp
        xref    th_remth
        xref    th_exit
*
        xdef    th_rthg
*
*+++
* This removes a thing from the thing list, as long as the thing is not in use
* at the moment.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0, FEX, IMEM
*       A0      name of thing
*       A6      system variables                preserved
*---
th_rthg
        jsr     th_find(pc)             ; is there a thing of this name?
        bne.s   thr_exit                ; no, bother
        jsr     th_remth(pc)            ; yes, remove it if possible
        bne.s   thr_exit                ; couldn't
        assert  th_nxtth,0
        move.l  a1,a0                   ; point to linkage
        lea     sys_thgl(a6),a1         ; list it's linked into
        move.w  mem.rlst,a2             ; unlink this thing
        jsr     (a2)
        move.l  a0,a1                   ; return thing linkage
        moveq   #0,d0
*
thr_exit
        jmp     th_exit(pc)
*
        end

