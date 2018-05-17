*       Get information on a channel's owner
*
*       Registers:
*               Entry                           Exit
*       D2      pointer to channel defn block   preserved
*       A0      pointer to row entry            preserved
*       A2      pointer to object pointer       updated (+4)
*
        section setup
*
        include 'dev8_qram_keys'
        include 'dev8_ptr_keys'
*
        xref    cl_cpstr
*
        xdef    cl_gjbin
*
mkjbreg reg     d1-d3/a0-a2
jstk_a0 equ     $0c
*
cl_gjbin
        movem.l mkjbreg,-(sp)
        move.l  d2,a1
        move.l  ch_owner(a1),d1         ; get owning job's ID
        move.l  d1,cl_jobid(a0)         ; fill it in
        beq.s   mkj_sbas                ; it's 0, so owned by SuperBASIC
*
        moveq   #0,d2                   ; top of tree=system
        moveq   #mt.jinf,d0
        trap    #1                      ; and info on it
        tst.l   d0                      ; does it exist?
        bne.s   mkj_nojb                ; no, wow!
*
        addq.l  #6,a0                   ; point to flag
        cmp.w   #$4afb,(a0)+            ; is there one?
        beq.s   mkj_cpnm                ; yes, copy the name
*
        lea     anon(pc),a0             ; make name 'Anon'
        bra.s   mkj_cpnm
*
mkj_nojb
        lea     nojb(pc),a0             ; make name 'No owner'
        bra.s   mkj_cpnm
*
mkj_sbas
        lea     basic(pc),a0            ; make name 'SuperBASIC'
*
mkj_cpnm
        move.l  a0,a2                   ; from name
        move.l  jstk_a0(sp),a1          ; to... 
        add.w   #cl_jobnm+2,a1          ; ...job name area
        move.l  a1,a0                   ; keep that
        jsr     cl_cpstr(pc)            ; copy job's name
        sub.l   a0,a1                   ; it was this long
        move.w  a1,-(a0)                ; fill in length
        move.l  a0,d0
*
        movem.l (sp)+,mkjbreg
        move.l  d0,(a2)+                ; fill in object pointer
        rts
*
nojb    dc.w    njend-*-2
        dc.b    'No owner!!'
njend
*
basic   dc.w    sbend-*-2
        dc.b    'SuperBASIC'
sbend
*
anon    dc.w    anend-*-2
        dc.b    '*** Anon ***'
anend
        dc.w    0
*
        end
