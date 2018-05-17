* Zap Thing from list                   v0.00   Feb 1988  J.R.Oakley  QJUMP
*
        section thing
*
        include 'dev8_keys_chp'
        include 'dev8_keys_err'
        include 'dev8_keys_qlv'
        include 'dev8_keys_sys'
        include 'dev8_keys_thg'
*
        xref    th_find
        xref    th_rechp
        xref    th_frmth
        xref    th_exit
*
        xdef    th_zthg
        xdef    th_hrzap
*
*+++
* This removes a thing from the thing list, as well as any Jobs using the
* thing at the moment.  Note that it is the responsibility of the calling
* code to return the linkage block to the heap if necessary.
*
*       Registers:
*               Entry                           Exit
*       D0                                      0, FEX, IMEM
*       D6      current Job                     preserved
*       A0      name of thing
*       A1                                      linkage block
*       A6      system variables                preserved
*---
th_zthg
        jsr     th_find(pc)             ; is there a thing of this name?
        bne.s   thr_exit                ; no, bother
        move.l  a1,a0                   ; point to linkage
        bsr.s   thz_rmth                ; unlink and remove
*
thr_exit
        jmp     th_exit(pc)
*+++
* Zap a Thing because its owner has been forcibly removed: this is a "close"
* routine pointed to as a Thing linkage block's "driver".
*
*       Registers:
*               Entry                           Exit
*       A0      linkage heap header
*       A1-A3                                   smashed
*       A6      system variables                preserved
*---
th_hrzap
hrzreg  reg     d6
        movem.l hrzreg,-(sp)
        moveq   #0,d6                   ; pretend current job is system
        lea     chp_end(a0),a0          ; point to usual linkage block
        bsr.s   thz_rmth
        movem.l (sp)+,hrzreg
        rts
*
thz_rmth
        lea     sys_thgl(a6),a1
        move.w  mem.rlst,a2
        jsr     (a2)                    ; unlink from the Thing list
        move.l  a0,a1
        jsr     th_frmth(pc)            ; force remove the Thing
        moveq   #0,d0                   ; no problems from "close"
        rts
*
        end


