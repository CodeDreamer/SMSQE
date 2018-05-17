; ATARI:  PEROM Utilities

        section perom

        xdef    pe_alloc
        xdef    pe_setup
        xdef    pe_done

        xref    gu_achp0

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_sys'
        include 'dev8_keys_err'
        include 'dev8_keys_atari'
        include 'dev8_keys_atari_perom'

;+++
; Allocate a window for PEROM programming
;
;       a0  r   base of window
;       d7  r   allocation
;
;       Status return standard
;---
pe_alloc
        move.l  #$11000,d0
        jsr     gu_achp0                 ; our window
        bne.s   pea_rts
        move.l  a0,d7                    ; base
        move.w  #$8000,d7                ; xxxx8000
        cmp.l   a0,d7                    ; in memory block
        bhs.s   pea_done
        add.l   #$10000,d7               ; no, but should be now
pea_done
        exg     d7,a0
        moveq   #0,d0
pea_rts
        rts

;+++
; Setup PEROM registers: called in supervisor mode
;
;       turns off cache
;
;       d1 c  p bank number
;       d6  r   msword, msbyte port status, lsbyte cache on flag
;       a0 c  p write window
;       a3  r   read mode
;       a4  r   set address mode
;       a5  r   write mode
;       a6  r   address of sector write
;
;---
pe_setup
pes.reg reg     d1/d2/a0
        movem.l pes.reg,-(sp)

        lea     pep_end,a3               ; program sector code
        lea     $400+pep_end-pep_prog(a0),a6 ; top of copied code

        moveq   #(pep_end-pep_prog)/2-1,d0
pes_pcopy
        move.w  -(a3),-(a6)
        dbra    d0,pes_pcopy

        moveq   #sms.info,d0
        trap    #do.sms2

        moveq   #0,d6
        move.w  sr,d0                    ; turn off cache
        or.w    #$0700,sr
        move.b  #gen.ctls,gen_ctls       ; temporary ST+16M version
        move.b  gen_ctlr,d6
        move.b  d6,d1
        bclr    #gen..cch,d1
        move.b  d1,gen_ctlw
        move.w  d0,sr

        lsl.w   #8,d6

        move.b  #sys.mtyp,d0
        and.b   sys_mtyp(a0),d0
        cmp.b   #sys.mste,d0             ; STE (should check for cache)
        blo.s   pes_sreg

        bclr    #0,at_stesp              ; clear cache
        sne     d6                       ; keep flag

pes_sreg
        swap    d6                       ; save cache flags
        movem.l (sp)+,pes.reg

        move.w  d1,d0
        lsl.w   #4,d0                    ; bank select bits
        lea     pe_read,a3
        add.w   d0,a3
        lea     pe_shaddr,a4
        add.w   d0,a4
        lea     pe_write,a5
        add.w   d0,a5

pes_exit
        moveq   #0,d0
        rts

;+++
; PEROM done - called in supervisor mode
;       resets the cache
;       d6  r   zero
;       status returned according to d0
;+++
pe_done
        move.l  d0,-(sp)
        rol.l   #8,d6                    ; old control value

        move.w  sr,d0
        or.w    #$0700,sr
        move.b  #gen.ctls,gen_ctls       ; temporary ST+16M version
        move.b  d6,gen_ctlw
        move.w  d0,sr

        tst.l   d6                       ; cache on?
        bpl.s   ped_exit
        bset    #0,at_stesp              ; set cache
ped_exit
        moveq   #0,d6
        move.l  (sp)+,d0
        rts

;+++
; PEROM program
;
;       d3 c  p word segment length
;       a1 c  u pointer to data
;       a2 c  u pointer to ROM
;       other regs as set up by pe_alloc (a0) and pe_setup (a3,a4,a5)
;---
pep_prog
        movem.l a1/a2,-(sp)
        move.w  d3,d0
        lsr.w   #2,d0
        subq.w  #1,d0                    ; compare blocks of 4
pep_ckl
        cmpm.l  (a1)+,(a2)+              ; is the data the same?
        dbne    d0,pep_ckl
        bne.s   pep_do                   ; data needs to be reprogrammed
        addq.l  #8,sp
        moveq   #0,d0
        rts

pep_do
        movem.l (sp)+,a1/a2
        movem.l d1-d7/a0/a6,-(sp)        ; write (8..32)*32 bytes

        move.w  sr,-(sp)
        or.w    #$0700,sr

        move.l  a2,d0
        sub.l   #pe_rbase,d0
        lsr.l   #8,d0                    ; upper address
        bclr    #0,d0

        tst.b   (a4)                     ; set access
        move.w  d0,(a0,d0.l)             ; set upper address
        tst.b   (a3)                     ; unset access

        move.w  a2,d0                    ; add lower address to a0
        and.w   #$01fe,d0                ; only works for 128/256 byte sectors
        add.w   d0,a0                    ; address in window

        add.w   d3,a2                    ; next address

        move.w  d3,d0
        lsr.w   #5,d0
        subq.w  #1,d0                    ; (8..32) 32 byte writes
pep_loop
        movem.l (a1)+,d1-d7/a6           ; data to write
        tst.b   (a5)                     ; set access
        movem.l  d1-d7/a6,(a0)           ; write to window
        tst.b   (a3)                     ; unset access
        add.w   #32,a0
        dbra    d0,pep_loop

        move.w  -2(a1),d0

        move.w  (sp)+,sr

        move.w  #2000,d1
        dbra    d1,*                     ; wait 1ish miliseconds

pep_wait
        cmp.w   -2(a2),d0
        dbeq    d1,pep_wait
        beq.s   pep_ok
        moveq   #err.nc,d0
        bra.s   pep_exit

pep_ok
        moveq   #0,d0
pep_exit
        movem.l (sp)+,d1-d7/a0/a6
        rts
pep_end

        end
