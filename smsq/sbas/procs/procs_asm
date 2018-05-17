; SBAS_PROCS_PROCS - Standard SBASIC Procedures V2.05	 1992	Tony Tebby
;
; 2005-12-10  2.03  Added POKE_F, PEEK_F (MK)
; 2006-06-28  2.04  Added YEAR%, MONTH%, DAY%, WEEKDAY%
; 2016-04-16  2.05  Added ALPHA_BLEND (MK)

	section procs

	xdef	sb_procs

	include 'dev8_keys_sbasic'
	include 'dev8_mac_proc'

chk_heap
	move.l	sb_chkhp(a6),a0
	jmp	(a0)			 ; heap checking patch

sb_procs
	proc_stt
	proc_def PRINT
	proc_def RUN
	proc_def STOP
	proc_def INPUT

	proc_def COLOUR_QL
	proc_def COLOUR_PAL
	proc_def COLOUR_24
	proc_def COLOUR_NATIVE

	proc_def BGCOLOUR_QL
	proc_def BGCOLOUR_24
	proc_def BGIMAGE

	proc_def PALETTE_QL
	proc_def PALETTE_8

	proc_def WINDOW
	proc_def BORDER
	proc_def INK
	proc_def STRIP
	proc_def PAPER
	proc_def BLOCK
	proc_def PAN
	proc_def SCROLL
	proc_def CSIZE
	proc_def FLASH
	proc_def UNDER
	proc_def OVER
	proc_def CURSOR
	proc_def AT
	proc_def ALPHA_BLEND

	proc_def SCALE
	proc_def POINT
	proc_def LINE
	proc_def ELLIPSE
	proc_def CIRCLE,ELLIPSE
	proc_def ARC
	proc_def POINT_R

	proc_def TURN
	proc_def TURNTO
	proc_def PENUP
	proc_def PENDOWN
	proc_def MOVE

	proc_def LIST
	proc_def OPEN		 ; channel open/close
	proc_def CLOSE
	proc_def FORMAT
	proc_def COPY
	proc_def COPY_N
	proc_def DELETE
	proc_def DIR		 ; filing system maintenance
	proc_def EXEC,exsb
	proc_def EXEC_W,ewsb
	proc_def LBYTES
	proc_def SEXEC
	proc_def SBYTES
	proc_def SAVE
	proc_def MERGE
	proc_def MRUN
	proc_def LOAD
	proc_def LRUN
	proc_def NEW
	proc_def CLEAR

	proc_def OPEN_IN
	proc_def OPEN_NEW

	proc_def CLS
	proc_def CALL,callsq
	proc_def RECOL
	proc_def RANDOMISE
	proc_def PAUSE
	proc_def POKE$
	proc_def POKE
	proc_def POKE_W
	proc_def POKE_L
	proc_def POKE_F
	proc_def BAUD
	proc_def BEEP
	proc_def CONTINUE
	proc_def RETRY
	proc_def READ
	proc_def NET
	proc_def MODE
	proc_def RENUM
	proc_def DLINE
	proc_def SDATE
	proc_def ADATE

	proc_def LINE_R
	proc_def ELLIPSE_R
	proc_def CIRCLE_R,ELLIPSE_R
	proc_def ARC_R

	proc_def AUTO,ed
	proc_def EDIT,ed

	proc_def FILL
	proc_def WIDTH
	proc_def REPORT

	proc_def QMERGE
	proc_def QMRUN
	proc_def QLOAD
	proc_def QLRUN
	proc_def QSAVE
	proc_def QSAVE_O

	proc_def SBASIC
	proc_def QUIT
	proc_def JOB_NAME

	proc_def TK2_EXT

	proc_def LRESPR,lrespr
	proc_def EPROM_LOAD
	proc_def TRA
	proc_def PROT_DATE
	proc_def PROT_MEM
	proc_def SLUG
	proc_def CACHE_ON
	proc_def CACHE_OFF
	proc_def IO_PRIORITY
	proc_def LANG_USE
	proc_ref CHK_HEAP
	proc_end

	proc_stt
	proc_def ACOS
	proc_def ACOT
	proc_def ASIN
	proc_def ATAN
	proc_def COS
	proc_def COT
	proc_def EXP
	proc_def LN
	proc_def LOG10
	proc_def SIN
	proc_def SQRT
	proc_def TAN
	proc_def DEG
	proc_def RAD
	proc_def RND
	proc_def INT
	proc_def ABS
	proc_def PI
	proc_def PEEK$
	proc_def PEEK
	proc_def PEEK_W
	proc_def PEEK_L
	proc_def PEEK_F

	proc_def RESPR
	proc_def EOF
	proc_def EOFW
	proc_def INKEY$
	proc_def CHR$
	proc_def CODE
	proc_def KEYROW
	proc_def BEEPING
	proc_def LEN
	proc_def DIMN
	proc_def DAY$
	proc_def DATE
	proc_def DATE$
	proc_def YEAR%,year
	proc_def MONTH%,month
	proc_def DAY%,day
	proc_def WEEKDAY%,weekday
	proc_def FILL$
	proc_def VER$

	proc_def ERR_NC,err$nc
	proc_def ERR_NJ,err$nj
	proc_def ERR_OM,err$om
	proc_def ERR_OR,err$or
	proc_def ERR_BO,err$bo
	proc_def ERR_NO,err$no
	proc_def ERR_NF,err$nf
	proc_def ERR_EX,err$ex
	proc_def ERR_IU,err$iu
	proc_def ERR_EF,err$ef
	proc_def ERR_DF,err$df
	proc_def ERR_BN,err$bn
	proc_def ERR_TE,err$te
	proc_def ERR_FF,err$ff
	proc_def ERR_BP,err$bp
	proc_def ERR_FE,err$fe
	proc_def ERR_XP,err$xp
	proc_def ERR_OV,err$ov
	proc_def ERR_NI,err$ni
	proc_def ERR_RO,err$ro
	proc_def ERR_BL,err$bl

	proc_def ERNUM
	proc_def ERLIN

	proc_def LANGUAGE
	proc_def LANGUAGE$

	proc_def DEVTYPE

	proc_end

	end
