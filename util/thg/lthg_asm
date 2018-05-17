* Link in new Thing                     v0.01   Feb 1988  J.R.Oakley  QJUMP
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
        xref    th_chekb
        xref    th_alchp
        xref    th_newth
        xref    th_exit
        xref    cv_lostr
*
        xdef    th_lthg
*
*+++
* This links a new thing into the thing list, as long a thing of the same
* name does not already exist in the list.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0, FEX, IMEM
*       D7      MSB set for fixed thing
*       A0              (name of thing)
*       A1      address of thing                preserved
*       A6      system variables                preserved
*---
th_lthg
thlreg  reg     a0/a1/a2
stk_a0  equ     $00
stk_a1  equ     $04
        movem.l thlreg,-(sp)
        lea     th_name(a1),a0           ; point to name
        jsr     th_find(pc)              ; is there already thing of this name?
        beq.s   thl_exex                 ; ... yes, bother
        jsr     th_chekb                 ; get check byte
        move.l  stk_a1(sp),a0            ; thing linkage
        move.b  d0,th_check(a0)          ; set check byte

        tst.l   d7                       ; fixed thing?
        bpl.s   thl_link                 ; ... no
        lea     th_name(a0),a1           ; ... yes, lowercase the name
        jsr     cv_lostr

thl_link
        assert  th_nxtth,0
        lea     sys_thgl(a6),a1          ; list to link into
        move.w  mem.llst,a2              ; link in this thing
        jsr     (a2)
        move.l  a0,a1                    ; restore linkage pointer
        jsr     th_newth(pc)             ; initialise usage list
        moveq   #0,d0                    ; no problem
thl_exit
        movem.l (sp)+,thlreg
        jmp     th_exit(pc)
thl_exex
        moveq   #err.fex,d0
        bra.s   thl_exit
*
        end
